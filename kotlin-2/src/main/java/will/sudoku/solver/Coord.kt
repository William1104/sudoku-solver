package will.sudoku.solver

import will.sudoku.solver.Settings.regionSize
import will.sudoku.solver.Settings.size

data class Coord private constructor(val row: Int, val col: Int) {

    val index = row * size + col
    val region = run {
        val regionRow = row / regionSize
        val regionCol = col / regionSize
        regionRow * regionSize + regionCol
    }

    companion object {
        val all = sequence {
            for (row in 0 until size)
                for (col in 0 until size)
                    yield(Coord(row,col))
        }.toList().toTypedArray()

        operator fun invoke(row: Int, col: Int): Coord {
            return all[row * size + col]
        }
    }
}