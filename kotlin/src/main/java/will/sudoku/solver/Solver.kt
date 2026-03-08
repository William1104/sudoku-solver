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

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // Sample puzzle from the Java Solver main() method
            // Original: {7, 0, 0, 0, 4, 0, 2, 0, 0},
            //           {0, 0, 0, 5, 2, 0, 0, 0, 6},
            //           {0, 0, 0, 0, 0, 0, 5, 0, 0},
            //           {0, 7, 0, 0, 0, 0, 9, 6, 0},
            //           {0, 6, 0, 0, 0, 0, 0, 8, 0},
            //           {4, 2, 5, 0, 0, 0, 0, 0, 0},
            //           {0, 0, 0, 0, 0, 9, 0, 3, 1},
            //           {0, 0, 4, 0, 0, 7, 0, 0, 0},
            //           {1, 0, 0, 6, 0, 0, 0, 0, 0}
            val puzzleString = """
                7...4.2..
                ...52...6
                ......5..
                .7....96.
                .6....8..
                425......
                .....9.31
                ..4..7...
                1..6.....
            """.trimIndent()

            val board = BoardReader.readBoard(puzzleString)
            println("Solving:")
            println(board)
            println()

            val solver = Solver()
            val solvedBoard = solver.solve(board)

            if (solvedBoard != null) {
                println("Solved:")
                println(solvedBoard)
            } else {
                println("No solution found")
                System.exit(1)
            }
        }
    }
}