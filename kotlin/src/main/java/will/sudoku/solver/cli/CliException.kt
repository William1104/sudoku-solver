package will.sudoku.solver.cli

/**
 * Custom exception for CLI-related errors.
 *
 * This exception is used for errors that occur during argument parsing,
 * file reading, or other CLI-specific operations.
 *
 * @property message Human-readable error message
 * @property cause Optional underlying cause
 * @property exitCode Suggested exit code for the application (default: 1)
 */
class CliException(
    override val message: String,
    cause: Throwable? = null,
    val exitCode: Int = 1
) : Exception(message, cause)
