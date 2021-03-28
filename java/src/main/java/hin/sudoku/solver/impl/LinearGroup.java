package hin.sudoku.solver.impl;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class LinearGroup extends Group {
	public LinearGroup(String designation, int range) {
		super(designation, range);
	}

	@Override
	public boolean solveRemaining() {
		if (this.isSolved())
			return true;
		final AtomicBoolean valueUpdated = new AtomicBoolean(false);
		this.unsolvedValues.forEach(v -> {
			final var candidateByRegion = this.cells.stream()
					.filter(c -> !c.isSet())
					.filter(c -> c.isCandidateForCell(v))
					.collect(groupingBy(Cell::getRegionGroup));

			if (1 == candidateByRegion.size()) {
				final var sharedRegionGroup = candidateByRegion.keySet().iterator().next();
				final var cellsFromOtherGroups = this.cells.stream()
						.filter(c -> !c.isSet())
						.filter(c -> !sharedRegionGroup.equals(c.getRegionGroup()))
						.collect(toList());

				final AtomicBoolean cellChanged = new AtomicBoolean(false);
				cellsFromOtherGroups.forEach(c -> {
					final var changed = c.removeCandidateValue(v);
					if (changed)
						cellChanged.set(true);
				});

				if (cellChanged.get())
					valueUpdated.set(true);
			}
		});
		return super.solveRemaining() || valueUpdated.get();
	}
}
