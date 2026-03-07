package will.sudoku.solver

/**
 * Exception thrown when puzzle-related operations fail.
 *
 * ## Error Types
 * - **InvalidPuzzleFormatException**: Puzzle format is invalid
 * - **InvalidPuzzleDataException**: Puzzle data is corrupted or invalid
 * - **DuplicateSolutionException**: Puzzle has multiple solutions
 * - **UnsolvableException**: Puzzle cannot be solved (reached before start)
 * - **PuzzleTooHardException**: Puzzle exceeds difficulty threshold
 *
 * ## Usage
 * Thrown by BoardReader, BoardReader, or puzzle operations when:
 * - Puzzle format is invalid
 * - Puzzle data is corrupted
 * - Multiple solutions detected
 * - Puzzle cannot be solved
 * - Puzzle is too hard for configured settings
 *
 * ## Example
 * ```kotlin
 * try {
 *     val board = BoardReader.readBoard(puzzleString)
 * } catch (e: PuzzleException) {
 *     System.err.println("Puzzle error: ${e.message}")
 *     System.err.println("Suggested fix: ${e.suggestion}")
 * }
 * ```
 *
 * @property suggestion Human-readable suggestion for fixing the error
 * @property exitCode Suggested exit code for the application
 */
open class PuzzleException(
    message: String,
    val suggestion: String? = null,
    val exitCode: Int = 2,
    cause: Throwable? = null
) : RuntimeException(message, cause) {
    constructor(message: String, suggestion: String? = null, exitCode: Int) : this(message) {
        this.suggestion = suggestion
        this.exitCode = exitCode
    }

    constructor(
        message: String,
        suggestion: String?,
        exitCode: Int,
        cause: Throwable? = null
    ) : this(message, cause) {
        this.suggestion = suggestion
        this.exitCode = exitCode
    }
}
