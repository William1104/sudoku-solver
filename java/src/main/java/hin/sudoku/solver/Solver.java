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
				{7, 0, 0, 0, 4, 0, 2, 0, 0},
				{0, 0, 0, 5, 2, 0, 0, 0, 6},
				{0, 0, 0, 0, 0, 0, 5, 0, 0},
				{0, 7, 0, 0, 0, 0, 9, 6, 0},
				{0, 6, 0, 0, 0, 0, 0, 8, 0},
				{4, 2, 5, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 9, 0, 3, 1},
				{0, 0, 4, 0, 0, 7, 0, 0, 0},
				{1, 0, 0, 6, 0, 0, 0, 0, 0}
		};


		final var solver = new Solver();
		final Grid solvedGrid = solver.solve(3, grid);
		if (null == solvedGrid)
			System.exit(1);
		System.out.printf("Solved:%n%s", solvedGrid);
	}

	public Grid solve(final int groupSize, final int[][] grid) {
		final Grid g = new Grid(groupSize, grid);
		final int iterationCount = g.solve();
		if (iterationCount > 0)
			return g;
		return null;
	}
}