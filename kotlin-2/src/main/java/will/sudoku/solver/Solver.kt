package will.sudoku.solver

import will.sudoku.solver.Settings.eliminators

class Solver {

    fun solve(board: Board): Board? {
        return solve(board, 0)
    }

    fun solve(board: Board, depth: Int): Board? {
        if (!board.isValid()) return null
        if (board.isSolved()) return board

        val moves = sequence {
            val unresolvedCoord = board.unresolvedCoord()!!
            for (candidateValue in board.candidateValues(unresolvedCoord)) {
                yield(Pair(unresolvedCoord, candidateValue))
            }
        }

        return moves.map { move ->
            val newBoard = board.copy()
            newBoard.markValue(move.first, move.second)

            for (eliminator in eliminators) {
                eliminator.eliminate(newBoard)
            }

            solve(newBoard, depth + 1)
        }.firstOrNull { it != null }
    }
}