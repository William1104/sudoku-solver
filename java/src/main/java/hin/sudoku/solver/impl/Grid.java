package hin.sudoku.solver.impl;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Getter;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static hin.sudoku.solver.impl.GridUtil.cloneGrid;
import static hin.sudoku.solver.impl.GridUtil.deepCopy;

public class Grid {
	private final int groupLength;
	private final int groupSize;
	@Getter
	private final Cell[][] grid;
	@Getter(AccessLevel.PUBLIC)
	private final IntObjectHashMap<LinearGroup> colGroups = new IntObjectHashMap<>();
	@Getter(AccessLevel.PUBLIC)
	private final IntObjectHashMap<LinearGroup> rowGroups = new IntObjectHashMap<>();
	@Getter(AccessLevel.PUBLIC)
	private final HashMap<Coordinate, Group> regionGroups = new HashMap<>();

	public Grid(final int groupLength, int[][] initialGrid) {
		this.groupLength = groupLength;
		this.groupSize = groupLength * groupLength;
		Preconditions.checkArgument(initialGrid.length == groupSize, "number of rows != %d^2", groupLength);
		for (int i = 0; i < initialGrid.length; ++i)
			Preconditions.checkArgument(initialGrid[i].length == groupSize, "row %d length != %d^2", i, groupLength);

		final int sideLength = this.groupSize;

		this.grid = new Cell[sideLength][sideLength];

		for (int row = 0; row < sideLength; ++row) {
			final var rowCol = new Coordinate(0, row);
			final var curRowGroup = new LinearGroup(rowCol, "row-" + row, groupSize);
			rowGroups.put(row, curRowGroup);
			for (int col = 0; col < sideLength; ++col) {
				final var regionGroupX = (col / groupLength) * groupLength;
				final var regionGroupY = (row / groupLength) * groupLength;
				final var regionCoord = new Coordinate(regionGroupX, regionGroupY);
				final var curRegionGroup = this.regionGroups.computeIfAbsent(regionCoord, (coord) -> new Group(coord, "region" + coord, groupSize));

				final var curCol = col;
				final var curColGroup = this.colGroups.getIfAbsentPut(col, () -> new LinearGroup(new Coordinate(curCol, 0), "col-" + curCol, groupSize));
				this.grid[row][col] = new Cell(new Coordinate(col, row), groupSize, curRowGroup, curColGroup, curRegionGroup);
			}
		}

		for (int i = 0; i < initialGrid.length; ++i) {
			final var row = initialGrid[i];
			for (int j = 0; j < row.length; ++j)
				if (0 < row[j])
					this.grid[i][j].setInitialValue(row[j]);
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rowGroups.size(); ++i) {
			final var curRow = rowGroups.get(i);
			sb.append('|');
			for (final Cell cell : curRow.getCells()) {
				sb.append(cell).append('|');
			}
			sb.append('\n');
		}
		return sb.substring(0, sb.length() - 1); // take out the last new line
	}

	public int solve() {
		return this.solve(0);
	}

	private int solve(final int initIterationCount) {
		int iterationCount = initIterationCount;
		do {
			++iterationCount;
			try {
				final var loopCount = iterationCount - initIterationCount;
				if (loopCount > 82) {
					System.err.printf("=======%nSolve-loop got stuck%n");
					return -1 * iterationCount;
				}
				final boolean cellChanged = confirmSolvedCells();
				final boolean regionChanged = solveGroup(this.regionGroups.values());
				final boolean rowsChanged = solveGroup(this.rowGroups);
				final boolean colsChanged = solveGroup(this.colGroups);

				if (!(cellChanged || regionChanged || rowsChanged || colsChanged))
					break;
			}
			catch (Throwable t) {
				return -1 * iterationCount;
			}
		} while(! isSolved());
		if (this.isSolved())
			return iterationCount;

		final Cell leastCandidateCell = Arrays.stream(this.grid).flatMap(Arrays::stream)
				.filter(c -> !c.isSet())
				.min(Comparator.comparingInt(Cell::numCandidateValues)).get();
		final var coord = leastCandidateCell.getCoord();
		final int[][] origCloneGrid = cloneGrid(this.grid);
		for (int idx = leastCandidateCell.getCandidateValues().nextSetBit(0); idx >= 0; idx = leastCandidateCell.getCandidateValues().nextSetBit(idx + 1)) {
			final int candidateValue = idx + 1;
			final int[][] cloneGridForTest = deepCopy(origCloneGrid);
			cloneGridForTest[coord.getRow()][coord.getCol()] = candidateValue;
			final Grid g = new Grid(this.groupLength, cloneGridForTest);
			final int newIterationCount = g.solve(iterationCount);
			// not solved, try next value
			if (newIterationCount <= 0) {
				iterationCount = -1 * newIterationCount;
				continue;
			}
			// solved!
			for (int i = 0 ; i < g.grid.length; ++i ) {
				final Cell[] row = g.grid[i];
				for ( int j = 0; j < row.length; ++j ) {
					if ( !this.grid[i][j].isSet()) {
						this.grid[i][j].setValue(g.grid[i][j].answer(), null);
					}
				}
			}
			return newIterationCount;
		}

		return -1 * iterationCount;
	}

	private boolean confirmSolvedCells() {
		boolean cellChanged = false;
		for (final Cell[] row : this.grid) {
			for (final Cell curCell : row) {
				if (curCell.isSet())
					continue;
				if (curCell.singleCandidateRemaining()) {
					curCell.confirmCandidate();
					cellChanged = true;
				}
			}
		}
		return cellChanged;
	}

	private boolean solveGroup(final Iterable<? extends Group> groupsToSolve) {
		final var groupChanged = new AtomicBoolean(false);
		groupsToSolve.forEach(g -> {
			if (! g.isSolved() && g.solveRemaining()) {
				groupChanged.set(true);
			}
		});
		return groupChanged.get();
	}

	public boolean isSolved() {
		return this.regionGroups.values().stream().allMatch(Group::isSolved);
	}
}
