package hin.sudoku.solver.impl;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@ToString(of = {"designation"})
@EqualsAndHashCode(of = {"headCoord"})
public class Group implements Comparable<Group> {
	private final String designation;
	protected final ArrayList<Cell> cells;
	@Getter(AccessLevel.PUBLIC)
	protected final BitSet unsolvedValues;
	private final Coordinate headCoord;

	public Group(final Coordinate headCoord, final String designation, final int range) {
		this.headCoord = headCoord;
		this.designation = designation;
		this.unsolvedValues = new BitSet(range);
		this.unsolvedValues.set(0, range);
		this.cells = new ArrayList<>(range);
	}

	public void addCell(final Cell cell) {
		this.cells.add(cell);
	}

	public Iterable<Cell> getCells() {
		return Collections.unmodifiableList(this.cells);
	}

	private boolean isResolved(final int value) {
		return !this.unsolvedValues.get(value - 1);
	}

	public boolean cannotTake(final int value) {
		if (isResolved(value))
			return true;
		return !this.cells.stream().allMatch(c -> c.canRemoveCandidate(value));
	}

	public boolean isSolved() {
		return unsolvedValues.isEmpty();
	}

	public void take(final int value, final Cell source, final Group initiatingGroup) {
		if (isResolved(value)) {
			final String msg = String.format("Group %s: Trying to take value %d which is already set", this.designation, value);
			throw new IllegalStateException(msg);
		}
		final boolean permitted = this.cells.stream().allMatch(
				c -> c == source || c.canRemoveCandidate(value)
		);
		if (!permitted) {
			final String msg = String.format("Group %s: trying to take value %d from group, but failed", this.designation, value);
			throw new IllegalStateException(msg);
		}

		this.cells.forEach(c -> {
			if (source != c) {
				c.removeCandidateValue(value);
			}
		});
		if (initiatingGroup != this)
			this.unsolvedValues.clear(value - 1);
	}

	/**
	 * Go through group's unresolved values, inspect the cells to see if the value can be decided
	 *
	 * @return {@code true} if any value is taken
	 */
	public boolean solveRemaining() {
		if (this.isSolved())
			return false;
		final var candidatesEliminated = new AtomicBoolean(false);
		for (int idx = this.unsolvedValues.nextSetBit(0); idx >= 0; idx = this.unsolvedValues.nextSetBit(idx + 1)) {
			final int v = idx + 1;
			final boolean colGroupUpdated = eliminateCandidateByGroup(v, Cell::getColGroup);
			final boolean rowGroupUpdated = eliminateCandidateByGroup(v, Cell::getRowGroup);
			if (colGroupUpdated || rowGroupUpdated)
				candidatesEliminated.set(true);
		}
		final boolean sharedGroupEliminated = eliminateCandidateByMultipleSharedGroups();

		// confirm any cell with just 1 candidate value which is not set (because candidates are just eliminated)
		final boolean cellConfirmed = confirmSingleCandidateCells();

		return cellConfirmed || candidatesEliminated.get() || sharedGroupEliminated;
	}

	protected boolean eliminateCandidateByGroup(final int v, final Function<Cell, Group> groupFunction) {
		final Group me = this;
		final var candidatesByGroup = this.cells.stream()
				.filter(c -> !c.isSet())
				.filter(c -> c.isCandidateForCell(v))
				.collect(groupingBy(groupFunction));

		if (1 != candidatesByGroup.size())
			return false;
		final var sharedGroup = candidatesByGroup.keySet().iterator().next();
		if (sharedGroup == me)
			return false;

		final var cellsFromOtherGroups = this.cells.stream()
				.filter(c -> !c.isSet())
				.filter(c -> !sharedGroup.equals(groupFunction.apply(c)))
				.collect(toList());
		final var candidatesRemoved = new AtomicBoolean(false);
		cellsFromOtherGroups.forEach(c -> {
			final var changed = c.removeCandidateValue(v);
			if (changed)
				candidatesRemoved.set(true);
		});
		return candidatesRemoved.get();
	}

	/**
	 * if N cells all have the same N candidate values, then the rest of the cells in the same group can't
	 * have them.
	 *
	 * @return {@code true} if anything within the group changed
	 */
	protected boolean eliminateCandidateByMultipleSharedGroups() {
		final var sharedGroupEliminated = new AtomicBoolean(false);
		final var cellsGroupedByCandidateValues = this.cells.stream()
				.filter(c -> !c.isSet())
				.collect(groupingBy(Cell::getCandidateValues));

		for (final var kv : cellsGroupedByCandidateValues.entrySet()) {
			final var candidateValueSet = kv.getKey();
			final var cellsWithSameCandidateValues = kv.getValue();
			if (candidateValueSet.cardinality() != cellsWithSameCandidateValues.size())
				continue;

			Group.this.cells.stream()
					.filter(c -> !c.isSet())
					.filter(c -> !cellsWithSameCandidateValues.contains(c))
					.forEach(cell -> {
						for (int idx = candidateValueSet.nextSetBit(0); idx >= 0; idx = candidateValueSet.nextSetBit(idx + 1))
							if (cell.removeCandidateValue(idx + 1))
								sharedGroupEliminated.set(true);
					});
		}
		return sharedGroupEliminated.get();
	}

	protected boolean confirmSingleCandidateCells() {
		var cellConfirmed = false;
		for (int idx = this.unsolvedValues.nextSetBit(0); idx >= 0; idx = this.unsolvedValues.nextSetBit(idx + 1)) {
			final var v = idx + 1;
			final var candidateCells = this.cells.stream()
					.filter(c -> !c.isSet())
					.filter(c -> c.isCandidateForCell(v))
					.collect(toList());
			if (1 == candidateCells.size()) {
				final Cell theCell = candidateCells.iterator().next();
				if (!theCell.canRemoveCandidate(v)) {
					final var msg = String.format("Group %s: Only 1 candidate cell for value %d, but cannot take from cell %s", this.designation, v, theCell);
					throw new IllegalStateException(msg);
				}
				theCell.setValue(v, this);
				cellConfirmed = true;
				this.unsolvedValues.clear(idx);
			}
		}
		return cellConfirmed;
	}

	@Override
	public int compareTo(Group o) {
		final int cmp = this.headCoord.compareTo(o.headCoord);
		if (0 != cmp)
			return cmp;
		return this.designation.compareTo(o.designation);
	}
}
