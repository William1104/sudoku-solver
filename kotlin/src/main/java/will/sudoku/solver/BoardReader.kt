package will.sudoku.solver

import will.sudoku.solver.Settings.symbols
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.inputStream

class BoardReader {

    companion object {
        fun readBoard(path: Path) : Board {
            try {
                return readBoard(path.toFile().inputStream())
            } catch (ex: Exception) {
                throw RuntimeException("Failed to read $path as a board", ex)
            }
        }


        fun readBoard(inputStream: InputStream): Board {
            val values = inputStream.bufferedReader()
                .readLines()
                .flatMap {
                    it.map { c -> symbols.indexOf(c) }.filterNot { v -> v < 0 }
                }
                .toIntArray()
            return Board(values)
        }
    }
}