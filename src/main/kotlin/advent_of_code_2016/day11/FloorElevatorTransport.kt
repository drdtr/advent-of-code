package advent_of_code_2016.day11

import Util.readInputLines
import advent_of_code_2016.day11.FloorElevatorTransport.*
import advent_of_code_2016.day11.FloorElevatorTransport.BuildingState.*
import java.util.*
import java.util.regex.Pattern

/**
 * [Radioisotope Thermoelectric Generators](https://adventofcode.com/2016/day/11)
 *
 * - There is a building with 4 floors and an elevator moving between them.
 * - There are microchips and generators.
 *    - Each generator works with a certain isotope.
 *    - Each microchip is compatible with exactly one generator.
 *    - If the microchip is on the same floor with its compatible generator, then it's protected.
 *    - If the microchip is not protected and on the same floor with another generator, then it breaks
 * - The elevator
 *    - stops at each floor for enough time to move chips or generators
 *    - expects at least one piece of equipment (chip/generator) to be transported, and wouldn't move otherwise
 *    - can transport at most two pieces of equipment
 *
 * You need to transport all chips and generators to the 4th floor.
 * What is the minimum number of steps needed for that?
 *
 * ### Solution idea
 *
 * We consider the building states, i.e. elevator position and equipment on floors, as nodes of a directed graph,
 * and use BFS to find the shortest path to the target state where all the equipment is on the top floor.
 */
class FloorElevatorTransport {
    data class BuildingState(val equipmentOnFloors: EquipmentOnFloors, val elevatorFloor: Int = 0) {
        enum class Isotope {
            Hydrogen, Dilithium, Elerium, Lithium, Plutonium, Promethium, Ruthenium, Strontium, Thulium
        }

        sealed interface Equipment
        data class Generator(val isotope: Isotope) : Equipment {
            override fun toString() = "$isotope-Generator"
        }

        data class Microchip(val isotope: Isotope) : Equipment {
            override fun toString() = "$isotope-Microchip"
        }

        data class EquipmentOnFloors(
            val generatorsByFloor: List<Set<Isotope>>, val microchipsByFloor: List<Set<Isotope>>
        ) {
            init {
                require(generatorsByFloor.size == microchipsByFloor.size) {
                    "generatorsByFloor and microchipsByFloor must be of same size " + "because they represent equipment on the same number of floors."
                }
            }

            val numberOfFloors = generatorsByFloor.size

            companion object {
                fun of(equipmentSortedByFloor: List<Collection<Equipment>>) =
                    EquipmentOnFloors(generatorsByFloor = equipmentSortedByFloor.map { equipOnFloor ->
                        val generators = equipOnFloor.filterIsInstance<Generator>()
                        if (generators.isEmpty()) EnumSet.noneOf(Isotope::class.java)
                        else EnumSet.copyOf(generators.map { it.isotope })
                    }, microchipsByFloor = equipmentSortedByFloor.map { equipOnFloor ->
                        val microchips = equipOnFloor.filterIsInstance<Microchip>()
                        if (microchips.isEmpty()) EnumSet.noneOf(Isotope::class.java)
                        else EnumSet.copyOf(microchips.map { it.isotope })
                    })
            }
        }

        override fun toString(): String {
            val equipmentByFloor = equipmentOnFloors.generatorsByFloor.zip(equipmentOnFloors.microchipsByFloor)
                    .map { (generatorIsotopes, microchipIsotopes) ->
                        generatorIsotopes.map(::Generator) + microchipIsotopes.map(::Microchip)
                    }
            val equipmentByFloorStr =
                equipmentByFloor.mapIndexed { index, equipList -> index to "Floor $index: $equipList" }
                        .sortedByDescending { it.first }.joinToString(separator = "\n") { it.second }
            return """Building state: 
                |elevatorFloor: $elevatorFloor
                |$equipmentByFloorStr""".trimMargin()
        }
    }

    fun minStepsToMoveEquipmentToTopFloor(initialBuildingState: BuildingState): Int {
        val targetBuildingState = calcBuildingStateWithAllEquipmentOnTopFloor(initialBuildingState.equipmentOnFloors)
        println("Initial building state $initialBuildingState")
        println("Target building state $targetBuildingState")


        val q = java.util.ArrayDeque<Pair<BuildingState, Int>>()
        val visited = hashSetOf<BuildingState>()
        q.offer(initialBuildingState to 0)
        while (q.isNotEmpty()) {
            val (buildingState, dist) = q.poll()
            if (buildingState == targetBuildingState) return dist
            if (buildingState in visited) continue
            visited += buildingState

            // generate possible transitions to other states, where at least one and at most two pieces of equipment
            // are transported one floor up or down and check whether resulting states are valid
            val floor = buildingState.elevatorFloor
            val floorEquipmentList =
                buildingState.equipmentOnFloors.generatorsByFloor[floor].map { Generator(it) } + buildingState.equipmentOnFloors.microchipsByFloor[floor].map {
                    Microchip(it)
                }
            val nextBuildingStates = buildList {
                for (i in floorEquipmentList.indices) for (j in i until floorEquipmentList.size) {
                    val equipmentToBeMoved = setOf(i, j).map { floorEquipmentList[it] }
                    if (floor > 0) {
                        add(buildingState.moveEquipment(floor, floor - 1, equipmentToBeMoved))
                    }
                    if (floor < buildingState.equipmentOnFloors.numberOfFloors - 1) {
                        add(buildingState.moveEquipment(floor, floor + 1, equipmentToBeMoved))
                    }
                }
            }
            val validUnvisitedStates = nextBuildingStates.filter { it.isValid() }.filter { it !in visited }
            for (nextBuildingState in validUnvisitedStates) {
                q.offer(nextBuildingState to dist + 1)
            }
        }

        return -1 // target state is unreachable
    }

    private fun calcBuildingStateWithAllEquipmentOnTopFloor(equipmentOnFloors: EquipmentOnFloors): BuildingState {
        return BuildingState(
            EquipmentOnFloors(
                generatorsByFloor = buildList {
                    repeat(equipmentOnFloors.numberOfFloors - 1) {
                        add(EnumSet.noneOf(Isotope::class.java))
                    }
                    add(EnumSet.copyOf(equipmentOnFloors.generatorsByFloor.flatten()))
                },
                microchipsByFloor = buildList {
                    repeat(equipmentOnFloors.numberOfFloors - 1) {
                        add(EnumSet.noneOf(Isotope::class.java))
                    }
                    add(EnumSet.copyOf(equipmentOnFloors.microchipsByFloor.flatten()))
                },
            ), elevatorFloor = equipmentOnFloors.numberOfFloors - 1
        )
    }

    private fun BuildingState.moveEquipment(
        srcFloor: Int, destFloor: Int, equipmentToBeMoved: Iterable<Equipment>
    ): BuildingState {
        val generatorsOnSrcFloor = EnumSet.copyOf(equipmentOnFloors.generatorsByFloor[srcFloor])
        val generatorsOnDestFloor = EnumSet.copyOf(equipmentOnFloors.generatorsByFloor[destFloor])
        val microchipsOnSrcFloor = EnumSet.copyOf(equipmentOnFloors.microchipsByFloor[srcFloor])
        val microchipsOnDestFloor = EnumSet.copyOf(equipmentOnFloors.microchipsByFloor[destFloor])
        for (equip in equipmentToBeMoved) when (equip) {
            is Generator -> {
                generatorsOnSrcFloor.remove(equip.isotope)
                generatorsOnDestFloor.add(equip.isotope)
            }

            is Microchip -> {
                microchipsOnSrcFloor.remove(equip.isotope)
                microchipsOnDestFloor.add(equip.isotope)
            }
        }
        return BuildingState(
            elevatorFloor = destFloor, equipmentOnFloors = EquipmentOnFloors(
                generatorsByFloor = equipmentOnFloors.generatorsByFloor.mapIndexed { floor, generatorsOnFloor ->
                    when (floor) {
                        srcFloor  -> generatorsOnSrcFloor
                        destFloor -> generatorsOnDestFloor
                        else      -> EnumSet.copyOf(generatorsOnFloor) // equipment on other floors wasn't moved
                    }
                },
                microchipsByFloor = equipmentOnFloors.microchipsByFloor.mapIndexed { floor, microchipsOnFloor ->
                    when (floor) {
                        srcFloor  -> microchipsOnSrcFloor
                        destFloor -> microchipsOnDestFloor
                        else      -> EnumSet.copyOf(microchipsOnFloor) // equipment on other floors wasn't moved
                    }
                },
            )
        )
    }

    private fun BuildingState.isValid(): Boolean =
        (0 until equipmentOnFloors.generatorsByFloor.size).all { isValidFloor(it) }

    private fun BuildingState.isValidFloor(floor: Int): Boolean {
        val generators = equipmentOnFloors.generatorsByFloor[floor]
        if (generators.isEmpty()) {
            return true // there are no generators that could break any of the chips
        }
        val microchips = equipmentOnFloors.microchipsByFloor[floor]
        // since there are generators on the floor, then the set of microchip isotopes must be a subset
        // of the generator isotopes. Otherwise, there's at least one chip not protected by a corresponding generator,
        // so that this chip would be broken by other generators
        return generators.containsAll(microchips)
    }
}

private object FloorInputPatterns {
    val emptyFloor = Pattern.compile("The (\\w+) floor contains nothing relevant.")
    val nonEmptyFloorSplitToFloorAndEquip = Pattern.compile("The (\\w+) floor contains a (.*)\\.")
    val pieceOfEquipment = Pattern.compile("(\\w+)(?:-compatible)? (generator|microchip)")
}

private fun parseFloorEquipment(s: String): Pair<Int, List<Equipment>> {
    fun parseFloorNumberNormalizedToZeroBased(s: String) = when (s) {
        "first"  -> 0
        "second" -> 1
        "third"  -> 2
        "fourth" -> 3
        else     -> error("Unexpected floor number $s")
    }

    fun parseEquipment(equipStr: String) = with(FloorInputPatterns.pieceOfEquipment.matcher(equipStr)) {
        require(matches()) { "Not a valid equipment string: $equipStr" }
        val isotopeStr = group(1).replaceFirstChar { if (it.isLowerCase()) it.uppercase() else it.toString() }
        val isotope = Isotope.valueOf(isotopeStr)
        when (group(2)) {
            "generator" -> Generator(isotope)
            "microchip" -> Microchip(isotope)
            else        -> error("Unexpected equipment type $equipStr")
        }
    }

    with(FloorInputPatterns.emptyFloor.matcher(s)) {
        if (matches()) return parseFloorNumberNormalizedToZeroBased(group(1)) to emptyList()
    }
    with(FloorInputPatterns.nonEmptyFloorSplitToFloorAndEquip.matcher(s)) {
        if (matches()) {
            val floorNumber = parseFloorNumberNormalizedToZeroBased(group(1))
            val equipStrings = group(2).split(Regex(",?( and)? a "))
            val equipList = equipStrings.map(::parseEquipment)
            return floorNumber to equipList
        }
    }
    error("Invalid floor input: $s")
}

private fun readInput(inputFileName: String): EquipmentOnFloors {
    val lines = readInputLines(2016, 11, inputFileName)
    val equipmentSortedByFloor = lines.map(::parseFloorEquipment).sortedBy { it.first }.map { it.second }
    return EquipmentOnFloors.of(equipmentSortedByFloor)
}

private fun printResult(inputFileName: String) {
    val equipOnFloor = readInput(inputFileName)
    val solver = FloorElevatorTransport()

    //  Part 1
    val initialBuildingState1 = BuildingState(equipmentOnFloors = equipOnFloor)
    print(initialBuildingState1)
    val res1 = solver.minStepsToMoveEquipmentToTopFloor(initialBuildingState1)
    println("Minimum steps: $res1")

    // Part 2
    val initialBuildingState2 = BuildingState(equipmentOnFloors = with(equipOnFloor) {
        copy(
            generatorsByFloor = generatorsByFloor.mapIndexed { floor, equipSet ->
                if (floor > 0) equipSet else EnumSet.copyOf(equipSet + Isotope.Elerium + Isotope.Dilithium)
            },
            microchipsByFloor = microchipsByFloor.mapIndexed { floor, equipSet ->
                if (floor > 0) equipSet else EnumSet.copyOf(equipSet + Isotope.Elerium + Isotope.Dilithium)
            },
        )
    })
    print(initialBuildingState2)
    val res2 = solver.minStepsToMoveEquipmentToTopFloor(initialBuildingState2)
    println("Minimum steps: $res2")

}

fun main() {
    printResult("input.txt")
}