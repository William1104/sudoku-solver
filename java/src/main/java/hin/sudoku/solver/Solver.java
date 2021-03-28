package hin.sudoku.solver;

import hin.sudoku.solver.impl.Grid;

public class Solver {
	public static void main(final String[] args) {

//				{0, 0, 0, 0, 0, 0, 0, 0, 0},
//				{0, 0, 0, 0, 0, 0, 0, 0, 0},
//				{0, 0, 0, 0, 0, 0, 0, 0, 0},
//				{0, 0, 0, 0, 0, 0, 0, 0, 0},
//				{0, 0, 0, 0, 0, 0, 0, 0, 0},
//				{0, 0, 0, 0, 0, 0, 0, 0, 0},
//				{0, 0, 0, 0, 0, 0, 0, 0, 0},
//				{0, 0, 0, 0, 0, 0, 0, 0, 0},
//				{0, 0, 0, 0, 0, 0, 0, 0, 0}

		int[][] grid = {
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


		final var solver = new Solver();
		final var iterationCount = solver.solve(3, grid);
		if (iterationCount < 0)
			System.exit(1);
	}

	public int solve(final int groupSize, final int[][] grid) {
		final Grid g = new Grid(groupSize, grid);
		final int iterationCount = g.solve();
		return iterationCount;
	}
}