package hin.sudoku.solver.impl;

import java.util.concurrent.atomic.AtomicBoolean;

public class LinearGroup extends Group {
	public LinearGroup(final Coordinate headCoord, final String designation, final int range) {
		super(headCoord, designation, range);
	}

	@Override
	public boolean solveRemaining() {
		if (this.isSolved())
			return true;

		final var candidateEliminated = new AtomicBoolean(false);

		for (int idx = this.unsolvedValues.nextSetBit(0); idx >= 0; idx = this.unsolvedValues.nextSetBit(idx + 1)) {
			final var v = idx + 1;
			// if the candidate cells for a given value is only residing within a certain region group
			// we can remove that candidate value from other cells
			final var regionGroupUpdated = eliminateCandidateByGroup(v, Cell::getRegionGroup);
			if (regionGroupUpdated)
				candidateEliminated.set(true);
		}
		final var cellConfirmed = confirmSingleCandidateCells();
		return super.solveRemaining() || candidateEliminated.get() || cellConfirmed;
	}
}
