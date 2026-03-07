package will.sudoku.solver.cli

import will.sudoku.solver.Board
import will.sudoku.solver.Coord
import java.lang.System.lineSeparator

/**
 * Formatter for Sudoku board output in different formats.
 *
 * This object provides methods to format solved boards in various output formats.
 */
object BoardFormatter {

    /**
     * Formats a board in the specified output format.
     *
     * @param board The board to format
     * @param format The desired output format
     * @return Formatted string representation of the board
     */
    fun format(board: Board, format: CliConfig.OutputFormat): String {
        return when (format) {
            CliConfig.OutputFormat.DEFAULT -> formatDefault(board)
            CliConfig.OutputFormat.PIPE -> formatPipe(board)
            CliConfig.OutputFormat.COMPACT -> formatCompact(board)
        }
    }

    /**
     * Formats a board in the default visual format with separators.
     *
     * Uses Board.toString() which provides a nicely formatted grid with
     * visual separators for 3x3 regions.
     *
     * @param board The board to format
     * @return Default formatted board string
     */
    private fun formatDefault(board: Board): String {
        return board.toString()
    }

    /**
     * Formats a board in pipe format.
     *
     * Each row is output as a pipe-separated string without visual separators.
     * Example:
     * ```
     * .4.3.81..
     * 21..65...
     * 6.....7.
     * ```
     *
     * @param board The board to format
     * @return Pipe formatted board string
     */
    private fun formatPipe(board: Board): String {
        val size = will.sudoku.solver.Settings.size
        val builder = StringBuilder()

        for (row in 0 until size) {
            val rowBuilder = StringBuilder()
            for (col in 0 until size) {
                rowBuilder.append(board.symbolAt(Coord(row, col)))
            }
            builder.append(rowBuilder.toString()).append(lineSeparator())
        }

        return builder.toString()
    }

    /**
     * Formats a board in compact format.
     *
     * The entire board is represented as a single line of 81 characters
     * (9 rows × 9 columns).
     *
     * Example: `.4.3.81..21..65...6.....7...`
     *
     * @param board The board to format
     * @return Compact formatted board string (single line)
     */
    private fun formatCompact(board: Board): String {
        val size = will.sudoku.solver.Settings.size
        val builder = StringBuilder()

        for (row in 0 until size) {
            for (col in 0 until size) {
                builder.append(board.symbolAt(Coord(row, col)))
            }
        }

        return builder.toString()
    }
}
