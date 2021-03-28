package hin.sudoku.solver.impl;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.eclipse.collections.impl.set.mutable.primitive.IntHashSet;

import java.util.Collections;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@ToString(of = {"designation"})
@EqualsAndHashCode(of = {"headCoord"})
public class Group implements Comparable<Group> {
	private final String designation;
	protected final TreeSet<Cell> cells = new TreeSet<>();
	@Getter(AccessLevel.PUBLIC)
	protected final IntHashSet unsolvedValues;
	private final Coordinate headCoord;

	public Group(final Coordinate headCoord, final String designation, final int range) {
		this.headCoord = headCoord;
		this.designation = designation;
		this.unsolvedValues = new IntHashSet(IntStream.rangeClosed(1, range).toArray());
	}

	public void addCell(final Cell cell) {
		this.cells.add(cell);
	}

	public Iterable<Cell> getCells() {
		return Collections.unmodifiableSet(cells);
	}

	public boolean canTake(final int value) {
		if (!this.unsolvedValues.contains(value))
			return false;

		return this.cells.stream().allMatch(c -> c.canRemoveCandidate(value));
	}

	public boolean isSolved() {
		return unsolvedValues.isEmpty();
	}

	public void take(final int value, final Cell source, final Group initiatingGroup) {
		if (!this.unsolvedValues.contains(value)) {
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
			this.unsolvedValues.remove(value);
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
		this.unsolvedValues.forEach(v -> {
			boolean colGroupUpdated = eliminateCandidateByGroup(v, Cell::getColGroup);
			boolean rowGroupUpdated = eliminateCandidateByGroup(v, Cell::getRowGroup);
			if (colGroupUpdated || rowGroupUpdated)
				candidatesEliminated.set(true);
		});
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
			if (candidateValueSet.size() != cellsWithSameCandidateValues.size())
				continue;

			Group.this.cells.stream()
					.filter(c -> !c.isSet())
					.filter(c -> !cellsWithSameCandidateValues.contains(c))
					.forEach(c -> candidateValueSet.forEach(v -> {
						if (c.removeCandidateValue(v))
							sharedGroupEliminated.set(true);
					}));
		}
		return sharedGroupEliminated.get();
	}

	protected boolean confirmSingleCandidateCells() {
		var cellConfirmed = false;
		final var iter = this.unsolvedValues.intIterator();
		while (iter.hasNext()) {
			final var v = iter.next();
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
				iter.remove();
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
