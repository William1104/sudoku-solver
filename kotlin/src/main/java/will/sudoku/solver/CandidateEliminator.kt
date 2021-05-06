package will.sudoku.solver

interface CandidateEliminator {

    fun eliminate(board: Board): Boolean
}