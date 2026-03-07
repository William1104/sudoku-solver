package will.sudoku.solver

/**
 * Exception thrown when solver encounters an error or cannot solve a puzzle.
 *
 * ## Error Types
 * - **UnsolvableException**: Puzzle has no valid solution
 * - **TimeoutException**: Solver exceeded time limit
 * - **InconsistentStateException**: Board state is inconsistent
 * - **SolverErrorException**: Internal solver logic error
 *
 * ## Usage
 * Thrown by Solver when:
 * - Solving process fails
 * - Puzzle cannot be solved
 * - Time limit exceeded
 * - State corruption detected
 *
 * ## Example
 * ```kotlin
 * try {
 *     val solution = solver.solve(board)
 * } catch (e: SolverException) {
 *     System.err.println("Solver failed: ${e.message}")
 *     exitCode = e.exitCode
 * }
 * ```
 *
 * @property exitCode Suggested exit code for the application
 * @property message Description of the solver error
 * @param cause Optional underlying cause
 */
open class SolverException(
    message: String,
    val exitCode: Int = 1,
    cause: Throwable? = null
) : RuntimeException(message, cause)
