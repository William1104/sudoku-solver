package will.sudoku.solver

import will.sudoku.solver.Settings.regionSize

data class CoordinateGroup constructor(val coordinates: List<Coordinate>) {

    companion object {
        private val indices = (0 until Settings.size)
        private val verticalCoordinateGroups = indices.map { groupIndex -> CoordinateGroup(indices, groupIndex) }
        private val horizontalCoordinateGroups = indices.map { groupIndex -> CoordinateGroup(groupIndex, indices) }
        private val regionCoordinateGroups = indices.map {
            val regionRow = it / regionSize
            val regionCol = it % regionSize
            val rowStart = regionRow * regionSize
            val colStart = regionCol * regionSize
            val rows = rowStart until (rowStart + regionSize)
            val cols = colStart until (colStart + regionSize)
            CoordinateGroup(rows, cols)
        }

        val all = listOf<CoordinateGroup>()
            .plus(verticalCoordinateGroups)
            .plus(horizontalCoordinateGroups)
            .plus(regionCoordinateGroups)

        fun verticalOf(coordinate: Coordinate): CoordinateGroup {
            return verticalCoordinateGroups[coordinate.col]
        }

        fun horizontalOf(coordinate: Coordinate): CoordinateGroup {
            return horizontalCoordinateGroups[coordinate.row]
        }

        fun regionOf(coordinate: Coordinate): CoordinateGroup {
            return regionCoordinateGroups[coordinate.region]
        }

        fun of(coordinate: Coordinate): List<CoordinateGroup> {
            return listOf(verticalOf(coordinate), horizontalOf(coordinate), regionOf(coordinate))
        }

        operator fun invoke(coordinates: List<Coordinate>): CoordinateGroup {
            return CoordinateGroup(coordinates)
        }

        operator fun invoke(row: Int, cols: IntRange): CoordinateGroup {
            return invoke(row..row, cols)
        }

        operator fun invoke(rows: IntRange, col: Int): CoordinateGroup {
            return invoke(rows, col..col)
        }

        operator fun invoke(rows: IntRange, cols: IntRange): CoordinateGroup {
            return CoordinateGroup(
                sequence {
                    for (row in rows)
                        for (col in cols)
                            yield(Coordinate(row, col))
                }.toList()
            )
        }
    }
}
