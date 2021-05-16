package hin.sudoku.solver

import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class SolverTest {
    @ParameterizedTest
    @MethodSource("grids")
    fun doTest(groupSize: Int, grid: Array<IntArray?>?) {
        val solver = Solver()
        val solvedGrid = solver.solve(groupSize, grid)
        Assertions.assertThat(solvedGrid).describedAs("solved grid").isNotNull
        System.out.printf("Solved:%n%s", solvedGrid)
    }

    fun grids(): Stream<Arguments> {
        val g1 = arrayOf(
            intArrayOf(0, 4, 0, 3, 0, 8, 1, 0, 0),
            intArrayOf(2, 1, 0, 0, 6, 5, 0, 0, 0),
            intArrayOf(6, 0, 0, 0, 0, 0, 0, 7, 0),
            intArrayOf(9, 0, 3, 0, 4, 6, 7, 8, 1),
            intArrayOf(1, 0, 4, 8, 2, 9, 5, 0, 6),
            intArrayOf(8, 0, 5, 0, 0, 0, 0, 2, 0),
            intArrayOf(4, 0, 0, 0, 0, 0, 6, 0, 3),
            intArrayOf(0, 0, 0, 6, 0, 2, 0, 4, 7),
            intArrayOf(0, 8, 0, 0, 3, 0, 0, 0, 0)
        )
        val g2 = arrayOf(
            intArrayOf(1, 0, 0, 4, 9, 6, 0, 0, 2),
            intArrayOf(3, 0, 6, 0, 1, 0, 7, 0, 0),
            intArrayOf(0, 8, 0, 0, 0, 3, 1, 0, 6),
            intArrayOf(0, 0, 5, 0, 6, 0, 0, 0, 8),
            intArrayOf(0, 6, 3, 0, 8, 5, 0, 0, 9),
            intArrayOf(0, 0, 0, 3, 0, 4, 5, 0, 1),
            intArrayOf(6, 0, 2, 0, 0, 0, 9, 0, 4),
            intArrayOf(8, 0, 0, 6, 0, 9, 0, 5, 0),
            intArrayOf(5, 0, 9, 8, 2, 0, 6, 0, 0)
        )
        val g3 = arrayOf(
            intArrayOf(0, 6, 0, 0, 0, 5, 0, 0, 0),
            intArrayOf(0, 7, 0, 0, 0, 0, 0, 0, 1),
            intArrayOf(0, 0, 0, 0, 6, 3, 4, 0, 0),
            intArrayOf(0, 0, 3, 0, 8, 0, 0, 0, 0),
            intArrayOf(2, 1, 0, 0, 9, 0, 0, 0, 5),
            intArrayOf(4, 0, 0, 0, 0, 7, 8, 0, 0),
            intArrayOf(0, 0, 1, 6, 0, 0, 0, 8, 4),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 5, 0),
            intArrayOf(8, 0, 0, 0, 4, 0, 6, 1, 0)
        )
        val hard = arrayOf(
            intArrayOf(7, 0, 0, 0, 4, 0, 2, 0, 0),
            intArrayOf(0, 0, 0, 5, 2, 0, 0, 0, 6),
            intArrayOf(0, 0, 0, 0, 0, 0, 5, 0, 0),
            intArrayOf(0, 7, 0, 0, 0, 0, 9, 6, 0),
            intArrayOf(0, 6, 0, 0, 0, 0, 0, 8, 0),
            intArrayOf(4, 2, 5, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 9, 0, 3, 1),
            intArrayOf(0, 0, 4, 0, 0, 7, 0, 0, 0),
            intArrayOf(1, 0, 0, 6, 0, 0, 0, 0, 0)
        )
        return Stream.of(
            Arguments.of(3, g1),
            Arguments.of(3, g2),
            Arguments.of(3, g3),
            Arguments.of(3, hard)
        )
    }
}