package hin.sudoku.solver.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.BitSet;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode(of = {"coord"})
public class Cell implements Comparable<Cell> {
	@Getter
	private final Coordinate coord;
	@Getter
	private final BitSet candidateValues;
	private final String toStringFormat;
	private final String paddingString;

	@Getter
	private final Group rowGroup;
	@Getter
	private final Group colGroup;
	@Getter
	private final Group regionGroup;

	@Getter
	private boolean isSet = false;

	public Cell(final Coordinate coord, final int range, final Group rowGroup, final Group colGroup, final Group regionGroup) {
		this.coord = coord;

		this.rowGroup = requireNonNull(rowGroup, "row group");
		this.colGroup = requireNonNull(colGroup, "column group");
		this.regionGroup = requireNonNull(regionGroup, "region group");

		this.rowGroup.addCell(this);
		this.colGroup.addCell(this);
		this.regionGroup.addCell(this);

		final int numDigit = (int) Math.floor(Math.log10(range)) + 1;
		this.toStringFormat = "%" + numDigit + "d";
		final var pad = new char[numDigit];
		for (int i = 0; i < numDigit; ++i)
			pad[i] = ' ';
		this.paddingString = new String(pad);
		this.candidateValues = new BitSet(range);
		this.candidateValues.set(0, range);
	}

	@Override
	public int compareTo(final Cell o) {
		return this.coord.compareTo(o.coord);
	}

	@Override
	public String toString() {
		if (isSet()) {
			final int value = this.candidateValues.nextSetBit(0) + 1;
			return String.format(this.toStringFormat, value);
		}
		else
			return this.paddingString;
	}

	public int answer() {
		if (!isSet()) {
			final var msg = String.format("Cell %s: Asking for answer but with possible values : %s", this.coord, this.candidateValues);
			throw new IllegalStateException(msg);
		}
		return this.candidateValues.nextSetBit(0) + 1;
	}

	public void setInitialValue(final int value) {
		if (this.isSet())
			throw new IllegalStateException(String.format("Cell %s: Already set!", this.coord));
		this.setValue(value, null);
	}

	public void setValue(final int value, final Group initiatingGroup) {
		if (this.canRemoveCandidate(value)) {
			if (this.rowGroup.cannotTake(value)) {
				final var msg = String.format("%d is already taken in row group %s", value, this.rowGroup);
				throw new IllegalArgumentException(msg);
			}
			if (this.colGroup.cannotTake(value)) {
				final var msg = String.format("%d is already taken in column group %s", value, this.rowGroup);
				throw new IllegalArgumentException(msg);
			}
			if (this.regionGroup.cannotTake(value)) {
				final var msg = String.format("%d is already taken in region group %s", value, this.rowGroup);
				throw new IllegalArgumentException(msg);
			}

			this.rowGroup.take(value, this, initiatingGroup);
			this.colGroup.take(value, this, initiatingGroup);
			this.regionGroup.take(value, this, initiatingGroup);
			this.candidateValues.clear();
			this.candidateValues.set(value - 1);
			this.isSet = true;
		}
	}

	public boolean singleCandidateRemaining() {
		return 1 == this.candidateValues.cardinality();
	}

	public void confirmCandidate() {
		if (!singleCandidateRemaining()) {
			final var msg = String.format("Cell %s: Attempting to confirm candidate, but with candidate remaining: %s", this.coord, this.candidateValues);
			throw new IllegalStateException(msg);
		}
		final var valueToSet = this.candidateValues.nextSetBit(0) + 1;
		this.setValue(valueToSet, null);
	}

	public boolean canRemoveCandidate(int value) {
		return !this.isSet() || !this.candidateValues.get(value - 1);
	}

	public boolean removeCandidateValue(final int candidateToRemove) {
		if (this.isSet() && candidateToRemove == this.answer()) {
			final var nsg = String.format("Cell %s: Trying to remove candidate value %d on set cell of same value", this.coord, candidateToRemove);
			throw new IllegalStateException(nsg);
		}

		final int idx = candidateToRemove - 1;
		final boolean wasSet = this.candidateValues.get(idx);
		this.candidateValues.clear(idx);
		return wasSet;
	}

	public boolean isCandidateForCell(final int value) {
		if (value > this.candidateValues.size())
			throw new IllegalArgumentException("value " + value + " is more than acceptable values of 1~" + this.candidateValues.size());
		return this.candidateValues.get(value - 1);
	}

	public int numCandidateValues() {
		return this.candidateValues.cardinality();
	}
}
