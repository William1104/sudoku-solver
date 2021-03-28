package hin.sudoku.solver.impl;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.eclipse.collections.impl.set.mutable.primitive.IntHashSet;

import java.util.Collections;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@ToString(of = {"designation"})
@EqualsAndHashCode(of = {"designation"})
public class Group {
	private final String designation;
	@Getter(AccessLevel.PUBLIC)
	protected final TreeSet<Cell> cells = new TreeSet<>();
	@Getter(AccessLevel.PUBLIC)
	protected final IntHashSet unsolvedValues;

	public Group(final String designation, final int range) {
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

		final AtomicBoolean cellChanged = new AtomicBoolean(false);
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
				cellChanged.set(true);
				iter.remove();
			}
		}

		return cellChanged.get();
	}
}
