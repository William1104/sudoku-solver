package hin.sudoku.solver.impl;

final class GridUtil {
	static int[][] cloneGrid(final Cell[][] sourceCells) {
		final var numRows = sourceCells.length;
		final var numCols = sourceCells[0].length;
		final int[][] cloneGrid = new int[numRows][numCols];
		for (int i = 0; i < numRows; ++i) {
			for (int j = 0; j < numCols; ++j) {
				final var srcCell = sourceCells[i][j];
				cloneGrid[i][j] = srcCell.isSet() ? srcCell.answer() : 0;
			}
		}
		return cloneGrid;
	}

	static int[][] deepCopy(final int[][] source) {
		final int[][] result = new int[source.length][source[0].length];
		for (int i = 0; i < source.length; ++i)
			System.arraycopy(source[i], 0, result[i], 0, source[0].length);
		return result;
	}
}
