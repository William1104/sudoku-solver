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
 *     if (e.suggestion != null) {
 *         System.err.println("Suggested fix: ${e.suggestion}")
 *     }
 * }
 * ```
 *
 * @property suggestion Human-readable suggestion for fixing the error
 * @property exitCode Suggested exit code for the application
 */
class PuzzleException(message: String, cause: Throwable? = null) : RuntimeException(message, cause) {

    var suggestion: String? = null
    var exitCode: Int = 2
}
