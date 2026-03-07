package will.sudoku.solver.cli

/**
 * Configuration for the CLI application.
 *
 * This data class holds all configuration options parsed from command-line arguments.
 *
 * @property showHelp Whether to display help message
 * @property showVersion Whether to display version information
 * @property format Output format (default, pipe, compact)
 * @property inputFiles List of input file paths (may be empty)
 * @property stringInput Optional puzzle string from --string argument
 * @property readStdin Whether to read from stdin (indicated by "-" argument)
 */
data class CliConfig(
    val showHelp: Boolean = false,
    val showVersion: Boolean = false,
    val format: OutputFormat = OutputFormat.DEFAULT,
    val inputFiles: List<String> = emptyList(),
    val stringInput: String? = null,
    val readStdin: Boolean = false
) {

    /**
     * Output format enumeration.
     */
    enum class OutputFormat {
        /** Default format with visual separators (Board.toString()) */
        DEFAULT,

        /** Pipe format: each row as a pipe-separated line */
        PIPE,

        /** Compact format: single line of 81 characters */
        COMPACT
    }

    /**
     * Checks if there is any input to process.
     *
     * @return true if there are input files, stdin flag is set, or string input is provided
     */
    fun hasInput(): Boolean {
        return inputFiles.isNotEmpty() || readStdin || stringInput != null
    }

    companion object {
        /**
         * Application version.
         */
        const val VERSION = "0.1.0"
    }
}
