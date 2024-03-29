package advent_of_code_2017.day14

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class KnotHashBitCountTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderKnotListBitCount::class)
    fun `test countBitsInKnotHashes`(expected: Int, key: String, numOfRows: Int) = with(KnotHashBitCount()) {
        val initNums = (0..255).toList()
        val lengthsSuffix = listOf(17, 31, 73, 47, 23)
        val lengthsList = (0 until numOfRows).map { n -> "$key-$n".toByteArray().map { it.toInt() } + lengthsSuffix }
        lengthsList.forEach { lengths -> println(toStringKnotHashBits(lengths, initNums)) }
        assertEquals(expected, countBitsInKnotHashes(lengthsList, initNums))
    }

    private class ArgumentsProviderKnotListBitCount : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(8108, "flqrgnkx", 128),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderKnotListConnectedComponentsCount::class)
    fun `test countConnectecComponents`(expected: Int, key: String, numOfRows: Int) = with(KnotHashBitCount()) {
        val initNums = (0..255).toList()
        val lengthsSuffix = listOf(17, 31, 73, 47, 23)
        val lengthsList = (0 until numOfRows).map { n -> "$key-$n".toByteArray().map { it.toInt() } + lengthsSuffix }
        assertEquals(expected, countConnectedComponents(lengthsList, initNums))
    }

    private class ArgumentsProviderKnotListConnectedComponentsCount : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(1242, "flqrgnkx", 128),
        )
    }

}