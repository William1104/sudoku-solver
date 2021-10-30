package will.sudoku.solver

import kotlin.math.sqrt

object Settings {
    const val size: Int = 9
    val regionSize: Int = sqrt(size.toDouble()).toInt()
    val symbols: CharArray = charArrayOf('.', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    val simpleCandidateEliminator = SimpleCandidateEliminator()
    val groupCandidateEliminator = GroupCandidateEliminator()
    val exclusionCandidateEliminator = ExclusionCandidateEliminator(9)
    val eliminators = listOf(simpleCandidateEliminator, groupCandidateEliminator, exclusionCandidateEliminator)

    init {
        require(regionSize * regionSize == size) { "given size [$size] cannot be properly sqrt into another integer" }
    }
}
