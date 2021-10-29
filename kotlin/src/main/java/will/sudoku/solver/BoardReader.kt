package will.sudoku.solver

import will.sudoku.solver.Settings.symbols
import java.io.IOException
import java.io.InputStream
import java.io.UncheckedIOException
import java.nio.file.Path

object BoardReader {

    @JvmStatic
    fun readBoard(path: Path): Board {
        try {
            return readBoard(path.toFile().inputStream())
        } catch (ex: IOException) {
            throw UncheckedIOException("Failed to read $path as a board", ex)
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
