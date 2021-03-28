package hin.sudoku.solver.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(of = {"x", "y"})

public class Coordinate implements Comparable<Coordinate> {
	@Getter
	private final int x;
	@Getter
	private final int y;
	private final String str;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
		this.str = String.format("(%3d,%3d)", x, y);
	}

	@Override
	public String toString() {
		return this.str;
	}

	@Override
	public int compareTo(final Coordinate o) {
		int comp = this.getX() - o.getX();
		if (0 != comp)
			return comp;
		return this.getY() - o.getY();
	}
}