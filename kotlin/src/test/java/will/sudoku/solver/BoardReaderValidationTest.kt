package will.sudoku.solver

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions

/**
 * Tests for BoardReader input validation.
 */
@DisplayName("BoardReader Validation Tests")
class BoardReaderValidationTest {

    @Test
    @DisplayName("Valid board should parse successfully")
    fun testValidBoard() {
        // Valid solved 9x9 board (from g1.solution)
        val boardString = "549378162217465398638291475923546781174829536865713924452987613391652847786134259"

        val board = BoardReader.readBoard(boardString)
        Assertions.assertEquals(81, board.candidatePatterns.size)
    }

    @Test
    @DisplayName("Empty board should throw exception")
    fun testEmptyBoard() {
        val boardString = ""

        val exception = Assertions.assertThrows(ValidationException::class.java) {
            BoardReader.readBoard(boardString)
        }

        Assertions.assertTrue(exception.message!!.contains("Expected 81 cells"))
        Assertions.assertTrue(exception.message!!.contains("but found"))
    }

    @Test
    @DisplayName("Board with only separators should throw exception")
    fun testOnlySeparators() {
        val boardString = "---!---!---"

        val exception = Assertions.assertThrows(ValidationException::class.java) {
            BoardReader.readBoard(boardString)
        }

        Assertions.assertTrue(exception.message!!.contains("Expected 81 cells"))
        Assertions.assertTrue(exception.message!!.contains("but found"))
    }
}
