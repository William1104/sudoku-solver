package will.sudoku.solver

class SimpleCandidateEliminator : CandidateEliminator {
    override fun eliminate(board: Board): Boolean {
        var anyUpdate = false
        var stable: Boolean
        do {
            stable = true
            for (coord in Coord.all) {
                val candidatePattern = board.candidatePattern(coord)
                if (candidatePattern.countOneBits() == 1) {
                    for (coordGroup in CoordGroup.of(coord)) {
                        for (peerCoord in coordGroup.coords) {
                            val updated = coord != peerCoord &&
                                    board.eraseCandidatePattern(peerCoord, candidatePattern)
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