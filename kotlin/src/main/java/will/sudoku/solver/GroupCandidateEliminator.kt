package will.sudoku.solver

class GroupCandidateEliminator : CandidateEliminator {
    override fun eliminate(board: Board): Boolean {
        var anyUpdate = false
        var stable: Boolean
        do {
            stable = true
            for (coordGroup in CoordinateGroup.all) {
                // find candidate pattern which occurrence equals to its bit count
                val candidatePatterns = coordGroup.coordinates
                    .groupingBy { board.candidatePattern(it) }
                    .eachCount()
                    .filter {
                        val bitCount = it.key.countOneBits()
                        bitCount == it.value && bitCount != Settings.size
                    }
                    .keys

                for (candidatePattern in candidatePatterns) {
                    // and then exclude that pattern from other cells in the same group
                    val updated = coordGroup.coordinates
                        .filterNot { board.candidatePattern(it) == candidatePattern }
                        .map { board.eraseCandidatePattern(it, candidatePattern) }
                        .any { it }

                    if (updated) {
                        anyUpdate = true
                        stable = false
                    }
                }
            }
        } while (!stable)

        return anyUpdate

    }
}
