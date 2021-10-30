package will.sudoku.solver

import will.sudoku.solver.Board.Companion.masks

// This simple eliminator simply scan all confirmed values
// and the remove candidates of the same group
class ExclusionCandidateEliminator(var shortCircuitThreshold: Int) : CandidateEliminator {

    override fun eliminate(board: Board): Boolean {
        var anyUpdate = false
        var stable: Boolean
        do {
            stable = true
            for (coordGroup in CoordinateGroup.all) {

                val knownValues = coordGroup.coordinates.map { board.value(it) }.toSet()
                if (knownValues.size >= shortCircuitThreshold) continue

                // find if any 'candidate' appear only once in the group
                // if so, mark the cell having this candidate with that candidate.
                val candidatesValueAppearOnceOnly =
                    coordGroup.coordinates.flatMap { coord -> board.candidateValues(coord).asSequence() }
                        .groupingBy { it }
                        .eachCount()
                        .filterValues { it == 1 }
                        .filterKeys { !knownValues.contains(it) }
                        .keys

                for (candidateValue in candidatesValueAppearOnceOnly) {
                    for (coord in coordGroup.coordinates) {
                        if (board.candidatePattern(coord) and masks[candidateValue - 1] > 1) {
                            board.markValue(coord, candidateValue)
                            anyUpdate = true
                            stable = false
                        }
                    }
                }
            }
        } while (!stable)
        return anyUpdate
    }


}
