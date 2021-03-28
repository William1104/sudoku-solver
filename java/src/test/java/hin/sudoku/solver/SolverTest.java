package hin.sudoku.solver;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class SolverTest {
	public static Stream<Arguments> grids() {
		int[][] g1 = {
				{0, 4, 0, 3, 0, 8, 1, 0, 0},
				{2, 1, 0, 0, 6, 5, 0, 0, 0},
				{6, 0, 0, 0, 0, 0, 0, 7, 0},
				{9, 0, 3, 0, 4, 6, 7, 8, 1},
				{1, 0, 4, 8, 2, 9, 5, 0, 6},
				{8, 0, 5, 0, 0, 0, 0, 2, 0},
				{4, 0, 0, 0, 0, 0, 6, 0, 3},
				{0, 0, 0, 6, 0, 2, 0, 4, 7},
				{0, 8, 0, 0, 3, 0, 0, 0, 0}
		};
		int[][] g2 = {
				{1, 0, 0, 4, 9, 6, 0, 0, 2},
				{3, 0, 6, 0, 1, 0, 7, 0, 0},
				{0, 8, 0, 0, 0, 3, 1, 0, 6},
				{0, 0, 5, 0, 6, 0, 0, 0, 8},
				{0, 6, 3, 0, 8, 5, 0, 0, 9},
				{0, 0, 0, 3, 0, 4, 5, 0, 1},
				{6, 0, 2, 0, 0, 0, 9, 0, 4},
				{8, 0, 0, 6, 0, 9, 0, 5, 0},
				{5, 0, 9, 8, 2, 0, 6, 0, 0}
		};
		int[][] g3 = {
				{0, 6, 0, 0, 0, 5, 0, 0, 0},
				{0, 7, 0, 0, 0, 0, 0, 0, 1},
				{0, 0, 0, 0, 6, 3, 4, 0, 0},
				{0, 0, 3, 0, 8, 0, 0, 0, 0},
				{2, 1, 0, 0, 9, 0, 0, 0, 5},
				{4, 0, 0, 0, 0, 7, 8, 0, 0},
				{0, 0, 1, 6, 0, 0, 0, 8, 4},
				{0, 0, 0, 0, 0, 0, 0, 5, 0},
				{8, 0, 0, 0, 4, 0, 6, 1, 0}
		};

		return Stream.of(
				Arguments.of(3, g1),
				Arguments.of(3, g2),
				Arguments.of(3, g3)
		);
	}

	@ParameterizedTest
	@MethodSource("grids")
	public void doTest(final int groupSize, final int[][] grid) {
		final var solver = new Solver();
		assertThat(solver.solve(groupSize, grid)).isGreaterThan(0);
	}
}