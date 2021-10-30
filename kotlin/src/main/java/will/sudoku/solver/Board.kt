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
        @JvmStatic
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
    fun symbolAt(coordinate: Coordinate): Char {
        return symbols[value(coordinate)]
    }

    fun isConfirmed(coordinate: Coordinate): Boolean {
        return masks.any { candidatePattern(coordinate) == it }
    }

    fun isValid(): Boolean {
        return Coordinate.all.none { candidatePatterns[it.index] == 0 } &&
                CoordinateGroup.all.none { group ->
                    group.coordinates
                        .filter { isConfirmed(it) }
                        .groupingBy { candidatePattern(it) }
                        .eachCount()
                        .any { it.value > 1 }
                }
    }

    fun isSolved(): Boolean {
        return Coordinate.all.all { isConfirmed(it) }
    }

    //
    // methods for printing boards -------------------------------
    //
    override fun toString(): String {
        fun line(row: Int): String {
            return (0 until regionSize).map { region ->
                (0 until regionSize).map { regionCol ->
                    symbolAt(Coordinate(row, region * regionSize + regionCol))
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
    fun candidatePattern(coordinate: Coordinate): Int {
        return candidatePatterns[coordinate.index]
    }

    // return the candidate pattern as a list of value
    fun candidateValues(coordinate: Coordinate): IntArray {
        val candidate = candidatePattern(coordinate)
        return (0 until size).filter {
            candidate and masks[it] > 0
        }.map { it + 1 }.toIntArray()
    }

    // erase candidate pattern
    // if the pattern is updated, return true
    fun eraseCandidatePattern(coordinate: Coordinate, candidatePattern: Int): Boolean {
        val old = candidatePatterns[coordinate.index]
        candidatePatterns[coordinate.index] = candidatePatterns[coordinate.index] and (candidatePattern.inv())
        return (old != candidatePatterns[coordinate.index])
    }

    // erase candidate value
    // if the pattern is updated, return true
    fun eraseCandidateValue(coordinate: Coordinate, value: Int): Boolean {
        return eraseCandidatePattern(coordinate, masks[value - 1])
    }

    //
    // methods for manipulating confirmed values --------------
    //

    // get confirmed value
    fun value(coordinate: Coordinate): Int {
        return (1..size).firstOrNull { candidatePatterns[coordinate.index] == masks[it - 1] } ?: 0
    }

    // mark confirmed value
    fun markValue(coordinate: Coordinate, value: Int) {
        candidatePatterns[coordinate.index] = masks[value - 1]
    }

    //
    // methods for getting unsolved coord ---------------------
    // this method should be handled by another delegated class for move selection later
    fun unresolvedCoord(): Coordinate? {
        return Coordinate.all.filterNot { isConfirmed(it) }
            .minByOrNull { candidatePattern(it).countOneBits() }
    }


}
