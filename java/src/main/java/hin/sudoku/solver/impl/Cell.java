package hin.sudoku.solver.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.eclipse.collections.api.set.primitive.MutableIntSet;
import org.eclipse.collections.impl.factory.primitive.IntSets;

import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode(of = {"coord"})
public class Cell implements Comparable<Cell> {
	private final Coordinate coord;
	@Getter
	private final MutableIntSet candidateValues;
	private final String toStringFormat;
	private final String paddingString;

	@Getter
	private final Group rowGroup;
	@Getter
	private final Group colGroup;
	@Getter
	private final Group regionGroup;

	private boolean isFixed = false;
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
		this.candidateValues = IntSets.mutable.withAll(IntStream.rangeClosed(1, range));
	}

	@Override
	public int compareTo(final Cell o) {
		return this.coord.compareTo(o.coord);
	}

	@Override
	public String toString() {
		if (isSet())
			return String.format(this.toStringFormat, this.candidateValues.max());
		else
			return this.paddingString;
	}

	public int answer() {
		if (!isSet()) {
			final var msg = String.format("Cell %s: Asking for answer but with possible values : %s", this.coord, this.candidateValues);
			throw new IllegalStateException(msg);
		}
		return this.candidateValues.max();
	}

	public void setInitialValue(final int value) {
		if (this.isFixed) {
			final var msg = String.format("Cell %s: Already set!", this.coord);
			throw new IllegalStateException(msg);
		}
		this.setValue(value, null);
		this.isFixed = true;
	}

	public void setValue(final int value, final Group initiatingGroup) {
		if (this.canRemoveCandidate(value)) {
			if (!this.rowGroup.canTake(value)) {
				final var msg = String.format("%d is already taken in row group %s", value, this.rowGroup);
				throw new IllegalArgumentException(msg);
			}
			if (!this.colGroup.canTake(value)) {
				final var msg = String.format("%d is already taken in column group %s", value, this.rowGroup);
				throw new IllegalArgumentException(msg);
			}
			if (!this.regionGroup.canTake(value)) {
				final var msg = String.format("%d is already taken in region group %s", value, this.rowGroup);
				throw new IllegalArgumentException(msg);
			}

			this.rowGroup.take(value, this, initiatingGroup);
			this.colGroup.take(value, this, initiatingGroup);
			this.regionGroup.take(value, this, initiatingGroup);
			this.candidateValues.removeIf(i -> i != value);
			this.isSet = true;
		}
	}

	public boolean singleCandidateRemaining() {
		return 1 == this.candidateValues.size();
	}

	public int confirmCandidate() {
		if (!singleCandidateRemaining()) {
			final var msg = String.format("Cell %s: Attempting to confirm candidate, but with candidate remaining: %s", this.coord, this.candidateValues);
			throw new IllegalStateException(msg);
		}
		final var valueToSet = this.candidateValues.max();
		this.setValue(valueToSet, null);
		return valueToSet;
	}

	public boolean canRemoveCandidate(int value) {
		return !this.isSet() || !this.candidateValues.contains(value);
	}

	public boolean removeCandidateValue(final int candidateToRemove) {
		if (this.isSet() && candidateToRemove == this.answer()) {
			final var nsg = String.format("Cell %s: Trying to remove candidate value %d on set cell of same value", this.coord, candidateToRemove);
			throw new IllegalStateException(nsg);
		}
		return this.candidateValues.remove(candidateToRemove);
	}

	public boolean isCandidateForCell(final int value) {
		return this.candidateValues.contains(value);
	}
}
