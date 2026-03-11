package will.sudoku.solver

/**
 * Interface for eliminators that support metrics collection.
 *
 * Eliminators implementing this interface can track their performance including
 * number of eliminations made, passes through the board, and time spent.
 */
interface MetricsEnabledEliminator : CandidateEliminator {

    /**
     * Eliminate candidates with metrics tracking.
     *
     * @param board The board to eliminate candidates from
     * @param metrics The metrics object to update
     * @return true if any eliminations were made
     */
    fun eliminateWithMetrics(board: Board, metrics: SolverMetrics): Boolean

    /**
     * Returns the name of this eliminator for metrics reporting.
     */
    fun getEliminatorName(): String
}
