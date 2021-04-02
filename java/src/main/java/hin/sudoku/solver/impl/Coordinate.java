package hin.sudoku.solver.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(of = {"row", "col"})
public class Coordinate implements Comparable<Coordinate> {
	@Getter
	private final int col;
	@Getter
	private final int row;
	private final String str;

	public Coordinate(int col, int row) {
		this.col = col;
		this.row = row;
		this.str = String.format("(%3d,%3d)", row, col);
	}

	@Override
	public String toString() {
		return this.str;
	}

	@Override
	public int compareTo(final Coordinate o) {
		int comp = this.getRow() - o.getRow();
		return (0 != comp) ? comp : (this.getCol() - o.getCol());
	}
}