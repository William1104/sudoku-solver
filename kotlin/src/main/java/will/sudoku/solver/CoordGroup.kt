package will.sudoku.solver

import will.sudoku.solver.Settings.regionSize
import will.sudoku.solver.Settings.size

data class CoordGroup private constructor(val coords: List<Coord>) {

    companion object {
        private val indices = (0 until Settings.size)
        private val verticalCoordGroups = indices.map { groupIndex -> CoordGroup(indices, groupIndex) }
        private val horizontalCoordGroups = indices.map { groupIndex -> CoordGroup(groupIndex, indices) }
        private val regionCoordGroups = indices.map {
            val regionRow = it / regionSize
            val regionCol = it % regionSize
            val rowStart = regionRow * regionSize
            val colStart = regionCol * regionSize
            val rows = rowStart until (rowStart + regionSize)
            val cols = colStart until (colStart + regionSize)
            CoordGroup(rows, cols)
        }

        val all = listOf<CoordGroup>()
            .plus(verticalCoordGroups)
            .plus(horizontalCoordGroups)
            .plus(regionCoordGroups)

        fun verticalOf(coord: Coord): CoordGroup {
            return verticalCoordGroups[coord.col]
        }

        fun horizontalOf(coord: Coord): CoordGroup {
            return horizontalCoordGroups[coord.row]
        }

        fun regionOf(coord: Coord): CoordGroup {
            return regionCoordGroups[coord.region]
        }

        fun of(coord: Coord): List<CoordGroup> {
            return listOf(verticalOf(coord), horizontalOf(coord), regionOf(coord))
        }

        operator fun invoke(coords: List<Coord>): CoordGroup {
            return CoordGroup(coords)
        }

        operator fun invoke(row: Int, cols: IntRange): CoordGroup {
            return invoke(row..row, cols)
        }

        operator fun invoke(rows: IntRange, col: Int): CoordGroup {
            return invoke(rows, col..col)
        }

        operator fun invoke(rows: IntRange, cols: IntRange): CoordGroup {
            return CoordGroup(
                sequence {
                    for (row in rows)
                        for (col in cols)
                            yield(Coord(row, col))
                }.toList()
            )
        }

        val verticalGroups = run {
            val groups = (0 until size).map {

            }
        }

    }
}
