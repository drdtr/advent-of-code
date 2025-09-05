package advent_of_code_2018.day08

import advent_of_code_2018.day08.TreeModifiedInorderTranservalReconstruction.*
import advent_of_code_2018.day08.TreeModifiedInorderTranservalReconstructionTest.ArgumentsProviders.ArgumentsProviderReconstructTree
import advent_of_code_2018.day08.TreeModifiedInorderTranservalReconstructionTest.ArgumentsProviders.ArgumentsProviderReconstructTreeAndCalcValueOfNode
import advent_of_code_2018.day08.TreeModifiedInorderTranservalReconstructionTest.ArgumentsProviders.ArgumentsProviderReconstructTreeAndSumOfMetadataEntries
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class TreeModifiedInorderTranservalReconstructionTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderReconstructTree::class)
    fun `test reconstructTree`(preorderNodesWithMetadata: List<Int>, expected: TreeNode) = with(
        TreeModifiedInorderTranservalReconstruction()
    ) {
        println(expected.toStringIndented())
        assertEquals(expected, reconstructTree(preorderNodesWithMetadata))
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderReconstructTreeAndSumOfMetadataEntries::class)
    fun `test reconstructTree + sumOfMetadataEntries`(preorderNodesWithMetadata: List<Int>, expected: Int) = with(
        TreeModifiedInorderTranservalReconstruction()
    ) {
        assertEquals(expected, sumOfMetadataEntries(reconstructTree(preorderNodesWithMetadata)))
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderReconstructTreeAndCalcValueOfNode::class)
    fun `test reconstructTree + calcValueOfNode`(preorderNodesWithMetadata: List<Int>, expected: Int) = with(
        TreeModifiedInorderTranservalReconstruction()
    ) {
        assertEquals(expected, calcValueOfNode(reconstructTree(preorderNodesWithMetadata)))
    }

    private object ArgumentsProviders {
        val preorderNodesWithMetadata = listOf(2, 3, 0, 3, 10, 11, 12, 1, 1, 0, 1, 99, 2, 1, 1, 2)

        class ArgumentsProviderReconstructTree : ArgumentsProvider {
            override fun provideArguments(p0: ExtensionContext?) = Stream.of(
                arguments(
                    preorderNodesWithMetadata,
                    TreeNode(
                        listOf(1, 1, 2), listOf(
                            TreeNode(
                                listOf(10, 11, 12), emptyList()
                            ),
                            TreeNode(
                                listOf(2), listOf(
                                    TreeNode(listOf(99), emptyList()),
                                )
                            ),
                        )
                    )
                ),
            )
        }

        class ArgumentsProviderReconstructTreeAndSumOfMetadataEntries : ArgumentsProvider {
            override fun provideArguments(p0: ExtensionContext?) = Stream.of(
                arguments(preorderNodesWithMetadata, 138),
            )
        }

        class ArgumentsProviderReconstructTreeAndCalcValueOfNode : ArgumentsProvider {
            override fun provideArguments(p0: ExtensionContext?) = Stream.of(
                arguments(preorderNodesWithMetadata, 66),
            )
        }
    }

}