package will.sudoku.solver.cli

import will.sudoku.solver.Board
import will.sudoku.solver.BoardReader
import will.sudoku.solver.Solver
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path

/**
 * Main execution logic for the CLI application.
 *
 * This object handles the core execution flow: reading puzzles, solving them,
 * and formatting the output.
 */
object CliRunner {

    /**
     * Runs the CLI application with the given configuration.
     *
     * @param config The CLI configuration
     * @return Exit code (0 for success, 1 for failure)
     */
    fun run(config: CliConfig): Int {
        // Handle special options first
        when {
            config.showHelp -> {
                println(CliParser.getHelpMessage())
                return 0
            }

            config.showVersion -> {
                println("Sudoku Solver version ${CliConfig.VERSION}")
                return 0
            }

            !config.hasInput() -> {
                System.err.println("Error: No input specified. Use --help for usage information.")
                return 1
            }
        }

        // Process input
        return try {
            when {
                config.stringInput != null -> {
                    processStringInput(config.stringInput, config.format)
                }

                config.readStdin -> {
                    processStdin(config.format)
                }

                config.inputFiles.isNotEmpty() -> {
                    processFiles(config.inputFiles, config.format)
                }

                else -> {
                    System.err.println("Error: No input specified")
                    1
                }
            }
        } catch (e: CliException) {
            System.err.println("Error: ${e.message}")
            e.exitCode
        } catch (e: Exception) {
            System.err.println("Error: ${e.message}")
            1
        }
    }

    /**
     * Processes a puzzle from a string input.
     *
     * @param puzzleString The puzzle string
     * @param format Output format
     * @return Exit code
     */
    private fun processStringInput(puzzleString: String, format: CliConfig.OutputFormat): Int {
        val board = BoardReader.readBoard(puzzleString)
        val solver = Solver()

        return when (val solution = solver.solve(board)) {
            null -> {
                System.err.println("No solution found")
                1
            }

            else -> {
                println(BoardFormatter.format(solution, format))
                0
            }
        }
    }

    /**
     * Processes puzzles from stdin.
     *
     * @param format Output format
     * @return Exit code
     */
    private fun processStdin(format: CliConfig.OutputFormat): Int {
        return processStream(System.`in`, format)
    }

    /**
     * Processes puzzles from an input stream.
     *
     * @param inputStream The input stream
     * @param format Output format
     * @return Exit code
     */
    private fun processStream(inputStream: InputStream, format: CliConfig.OutputFormat): Int {
        val board = BoardReader.readBoard(inputStream)
        val solver = Solver()

        return when (val solution = solver.solve(board)) {
            null -> {
                System.err.println("No solution found")
                1
            }

            else -> {
                println(BoardFormatter.format(solution, format))
                0
            }
        }
    }

    /**
     * Processes puzzles from multiple files.
     *
     * @param filePaths List of file paths
     * @param format Output format
     * @return Exit code (0 if all puzzles solved successfully, 1 if any failed)
     */
    private fun processFiles(filePaths: List<String>, format: CliConfig.OutputFormat): Int {
        val solver = Solver()
        var hasErrors = false

        for (filePath in filePaths) {
            try {
                val path = Path.of(filePath)

                // Check if file exists
                if (!Files.exists(path)) {
                    System.err.println("Error: File not found: $filePath")
                    hasErrors = true
                    continue
                }

                // Read and solve puzzle
                val board = BoardReader.readBoard(path)
                val solution = solver.solve(board)

                when (solution) {
                    null -> {
                        System.err.println("Error: No solution found for $filePath")
                        hasErrors = true
                    }

                    else -> {
                        // Print separator for multiple files
                        if (filePaths.size > 1) {
                            println("=== $filePath ===")
                        }
                        println(BoardFormatter.format(solution, format))

                        // Print separator between files
                        if (filePath != filePaths.last()) {
                            println()
                        }
                    }
                }
            } catch (e: Exception) {
                System.err.println("Error processing $filePath: ${e.message}")
                hasErrors = true
            }
        }

        return if (hasErrors) 1 else 0
    }
}
