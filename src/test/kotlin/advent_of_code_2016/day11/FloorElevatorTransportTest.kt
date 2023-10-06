package advent_of_code_2016.day11

import advent_of_code_2016.day10.BotSimulator.*
import advent_of_code_2016.day10.BotSimulatorTest.*
import advent_of_code_2016.day11.FloorElevatorTransport.*
import advent_of_code_2016.day11.FloorElevatorTransport.BuildingState.*
import advent_of_code_2016.day11.FloorElevatorTransport.BuildingState.Isotope.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class FloorElevatorTransportTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderFloorElevatorTransport::class)
    fun `test FloorElevatorTransport`(expectedMinSteps: Int, initialBuildingState: BuildingState) {
        assertEquals(expectedMinSteps, FloorElevatorTransport().minStepsToMoveEquipmentToTopFloor(initialBuildingState))
    }

    private class ArgumentsProviderFloorElevatorTransport : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(
                11,
                BuildingState(
                    equipmentOnFloors = EquipmentOnFloors.of(
                        listOf(
                            /* floor 0 */ listOf(Microchip(Hydrogen), Microchip(Lithium)),
                            /* floor 1 */ listOf(Generator(Hydrogen)),
                            /* floor 2 */ listOf(Generator(Lithium)),
                            /* floor 3 */ emptyList(),
                        )
                    )
                )
            ),
        )
    }
}