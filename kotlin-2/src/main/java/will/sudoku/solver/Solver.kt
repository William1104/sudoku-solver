package will.sudoku.solver

import will.sudoku.solver.Settings.eliminators

class Solver {

    fun solve(board: Board): Board? {
        return solve(board, 0)
    }

    fun solve(board: Board, depth: Int): Board? {
        if (board.isSolved()) return board

        val moves = sequence {
            val unresolvedCoord = board.unresolvedCoord()
            for (candidateValue in board.candidatePatternValue(unresolvedCoord)) {
                yield(Pair(unresolvedCoord, candidateValue))
            }
        }

        for (move in moves) {

            // clone a board, and mark a potential move
            val newBoard = board.copy()
            newBoard.markValue(move.first, move.second)

            // run eliminators
            eliminators.forEach { it.eliminate(newBoard) }

            if (!newBoard.isValid()) {
                continue;
            }

            val solved = solve(newBoard, depth + 1)
            if (solved != null) {
                return solved
            }
        }
        return null
    }
}