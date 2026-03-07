package will.sudoku.solver

import will.sudoku.solver.cli.CliParser
import will.sudoku.solver.cli.CliRunner

/**
 * Main entry point for the Sudoku solver CLI application.
 *
 * This application solves Sudoku puzzles from various input sources:
 * - File(s)
 * - Standard input (stdin)
 * - Command-line string argument
 *
 * Supported output formats:
 * - Default: Visual format with separators
 * - Pipe: Each row as a pipe-separated line
 * - Compact: Single line of 81 characters
 *
 * @param args Command-line arguments
 */
fun main(args: Array<String>) {
    try {
        val config = CliParser.parse(args)
        val exitCode = CliRunner.run(config)
        System.exit(exitCode)
    } catch (e: Exception) {
        System.err.println("Error: ${e.message}")
        System.exit(1)
    }
}
