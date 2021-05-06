package will.sudoku.solver

import will.sudoku.solver.Settings.regionSize
import will.sudoku.solver.Settings.size
import will.sudoku.solver.Settings.symbols
import java.lang.System.lineSeparator

class Board private constructor(val candidatePatterns: IntArray) {
    init {
        require(candidatePatterns.size == size * size)
    }

    companion object {
        // candidate masks. it helps to extract the candidate values from candidate pattern
        val masks = (0 until size).map { 1 shl it }.toIntArray()

        // constructs a Board with known values
        operator fun invoke(values: IntArray): Board {
            val candidatePatternForWildcard = (1 shl size) - 1
            val candidatePatterns = values.map {
                when (it) {
                    // if value is 0, it can be anything
                    0 -> candidatePatternForWildcard

                    // if value is 1, the candidate pattern in binary format should be 0b000000001
                    // if value is 4, the candidate pattern in binary format should be 0b000000100
                    else -> masks[it - 1]
                }
            }.toIntArray()
            return Board(candidatePatterns)
        }
    }

    //
    // methods for equals, hashCode and copy ---------------------
    //
    override fun equals(other: Any?): Boolean {
        return (other is Board) && candidatePatterns.contentEquals(other.candidatePatterns)
    }

    override fun hashCode(): Int {
        return candidatePatterns.contentHashCode()
    }

    fun copy(): Board {
        return Board(candidatePatterns.copyOf())
    }

    //
    // methods for status checking -------------------------------
    //
    fun symbolAt(coord: Coord): Char {
        return symbols[value(coord)]
    }

    fun isConfirmed(coord: Coord): Boolean {
        return masks.any { candidatePattern(coord) == it }
    }

    fun isValid(): Boolean {
        return Coord.all.none { candidatePatterns[it.index] == 0 } &&
                CoordGroup.all.none { group ->
                    group.coords
                        .filter { isConfirmed(it) }
                        .groupingBy { candidatePattern(it) }
                        .eachCount()
                        .any { it.value > 1 }
                }
    }

    fun isSolved(): Boolean {
        return Coord.all.all { isConfirmed(it) }
    }

    //
    // methods for printing boards -------------------------------
    //
    override fun toString(): String {
        fun line(row: Int): String {
            return (0 until regionSize).map { region ->
                (0 until regionSize).map { regionCol ->
                    symbolAt(Coord(row, region * regionSize + regionCol))
                }.joinToString(separator = "")
            }.joinToString(separator = "!") + lineSeparator()
        }

        val regionSegment = CharArray(regionSize) { '-' }.joinToString(separator = "")

        val sepLine = (0 until regionSize)
            .joinToString(separator = "!") { regionSegment } + lineSeparator()

        return (0 until regionSize).joinToString(separator = sepLine) { region ->
            (0 until regionSize).joinToString(separator = "") { regionRow ->
                line(region * regionSize + regionRow)
            }
        }
    }

    //
    // methods for manipulating candidate patterns ---------------
    //

    // return the raw candidate pattern
    fun candidatePattern(coord: Coord): Int {
        return candidatePatterns[coord.index]
    }

    // return the candidate pattern as a list of value
    fun candidateValues(coord: Coord): IntArray {
        val candidate = candidatePattern(coord)
        return (0 until size).filter {
            candidate and masks[it] > 0
        }.map { it + 1 }.toIntArray()
    }

    // erase candidate pattern
    // if the pattern is updated, return true
    fun eraseCandidatePattern(coord: Coord, candidatePattern: Int): Boolean {
        val old = candidatePatterns[coord.index]
        candidatePatterns[coord.index] = candidatePatterns[coord.index] and (candidatePattern.inv())
        return (old != candidatePatterns[coord.index])
    }

    // erase candidate value
    // if the pattern is updated, return true
    fun eraseCandidateValue(coord: Coord, value: Int): Boolean {
        return eraseCandidatePattern(coord, masks[value - 1])
    }

    //
    // methods for manipulating confirmed values --------------
    //

    // get confirmed value
    fun value(coord: Coord): Int {
        return (1..size).firstOrNull { candidatePatterns[coord.index] == masks[it - 1] } ?: 0
    }

    // mark confirmed value
    fun markValue(coord: Coord, value: Int) {
        candidatePatterns[coord.index] = masks[value - 1]
    }

    //
    // methods for getting unsolved coord ---------------------
    // this method should be handled by another delegated class for move selection later
    fun unresolvedCoord(): Coord? {
        return Coord.all.filterNot { isConfirmed(it) }
            .minByOrNull { candidatePattern(it).countOneBits() }
    }


}
