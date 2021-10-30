package will.sudoku.solver

class SimpleCandidateEliminator : CandidateEliminator {
    override fun eliminate(board: Board): Boolean {
        var anyUpdate = false
        var stable: Boolean
        do {
            stable = true
            for (coordGroup in CoordinateGroup.all) {
                for (coord in coordGroup.coordinates) {
                    if (board.isConfirmed(coord)) {
                        for (peerCoord in coordGroup.coordinates) {
                            val updated = (coord != peerCoord) &&
                                    board.eraseCandidatePattern(peerCoord, board.candidatePattern(coord))
                            if (updated) {
                                anyUpdate = true
                                stable = false
                            }
                        }
                    }
                }
            }
        } while (!stable)
        return anyUpdate
    }
}
