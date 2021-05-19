package will.sudoku.solver

import will.sudoku.solver.Settings.symbols
import java.io.InputStream
import java.nio.file.Path

class BoardReader {

    companion object {
        @JvmStatic
        fun readBoard(path: Path): Board {
            try {
                return readBoard(path.toFile().inputStream())
            } catch (ex: Exception) {
                throw RuntimeException("Failed to read $path as a board", ex)
            }
        }

        @JvmStatic
        fun readBoard(inputStream: InputStream): Board {
            return readBoard(inputStream.bufferedReader().readText())
        }

        @JvmStatic
        fun readBoard(string: String): Board {
            val values = sequence {
                string.forEach { c ->
                    val value = symbols.indexOf(c)
                    if (value >= 0) {
                        yield(value)
                    }
                }
            }.toList().toIntArray()
            return Board(values)
        }

    }
}