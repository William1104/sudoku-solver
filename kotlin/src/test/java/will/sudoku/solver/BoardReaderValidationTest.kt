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
        // Simple valid board
        val boardString = "..1....5..6..9"
        val board = BoardReader.readBoard(boardString)

        // Should not throw exception
        // Verify board has expected cells
        Assertions.assertEquals(81, board.candidatePatterns.size)
    }

    @Test
    @DisplayName("Valid board with separators should parse successfully")
    fun testValidBoardWithSeparators() {
        val boardString = """
            .4.!3.8!1..
            21.!.65!...
            6..!...!.7.
            ---!---!---
            ..5!...!6.
            .6...!4..
            .1...!9..
            8....!7.2
            1...!5..!
            --!-!-!-!-
        """.trimIndent()

        val board = BoardReader.readBoard(boardString)

        Assertions.assertEquals(81, board.candidatePatterns.size)
    }

    @Test
    @DisplayName("Board with too few cells should throw exception")
    fun testTooFewCells() {
        val boardString = "12" // Only 2 cells

        val exception = Assertions.assertThrows(ValidationException::class.java) {
            BoardReader.readBoard(boardString)
        }

        Assertions.assertTrue(exception.message!!.contains("Expected 81 cells"))
        Assertions.assertTrue(exception.message!!.contains("but found"))
    }

    @Test
    @DisplayName("Board with too many cells should throw exception")
    fun testTooManyCells() {
        // Create string with more than 81 valid cells
        val boardString = "1".repeat(82)

        val exception = Assertions.assertThrows(ValidationException::class.java) {
            BoardReader.readBoard(boardString)
        }

        Assertions.assertTrue(exception.message!!.contains("Expected 81 cells"))
        Assertions.assertTrue(exception.message!!.contains("but found"))
    }

    @Test
    @DisplayName("Board with duplicate value in row should throw exception")
    fun testDuplicateInRow() {
        // First row has two '1's
        val boardString = """
            12.3456789
            1.........
            345678912
            456789123
            567891234
            678912345
            789123456
            891234567
            912345678
            123456789
        """.trimIndent()

        val exception = Assertions.assertThrows(ValidationException::class.java) {
            BoardReader.readBoard(boardString)
        }

        Assertions.assertTrue(exception.message!!.contains("Duplicate value"))
    }

    @Test
    @DisplayName("Board with duplicate value in column should throw exception")
    fun testDuplicateInColumn() {
        // First column has two '1's
        val boardString = """
            1.3456789
            1.........
            345678912
            456789123
            567891234
            678912345
            789123456
            891234567
            912345678
            234567891
        """.trimIndent()

        val exception = Assertions.assertThrows(ValidationException::class.java) {
            BoardReader.readBoard(boardString)
        }

        Assertions.assertTrue(exception.message!!.contains("Duplicate value"))
    }

    @Test
    @DisplayName("Board with duplicate value in region should throw exception")
    fun testDuplicateInRegion() {
        // Top-left region has two '1's
        val boardString = """
            12.3456789
            ..1.......
            3456789.12
            456789123
            567891234
            678912345
            789123456
            891234567
            912345678
        """.trimIndent()

        val exception = Assertions.assertThrows(ValidationException::class.java) {
            BoardReader.readBoard(boardString)
        }

        Assertions.assertTrue(exception.message!!.contains("Duplicate value"))
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

    @Test
    @DisplayName("Board with mixed line endings should parse correctly")
    fun testMixedLineEndings() {
        val boardString = ".4.3\r\n.1.\r\n5.6.7.8.9\r\n" +
                "2.5.8\r\n.3.6.9\r\n1.7.4\r\n" +
                "8.2.5\r\n.9.4.3\r\n" +
                "6.7.2\r\n.5.8.6\r\n" +
                "9.4.7\r\n.1.2.5\r\n" +
                "3.6.8\r\n.5.9.4\r\n" +
                "7.2.1\r\n.8.3.6\r\n" +
                "4.5.9"

        val board = BoardReader.readBoard(boardString)

        Assertions.assertEquals(81, board.candidatePatterns.size)
    }

    @Test
    @DisplayName("Board with leading/trailing whitespace should parse correctly")
    fun testWhitespaceBoard() {
        val boardString = "   .4.3.1.5.6.7.8.9   \n" +
                "  2.5.8.3.6.9.1.7.4  \n  " +
                " 8.2.5.9.4.3.6.7  \n" +
                " 3.6.8.9.4.3.6.7.2  \n  " +
                " 7.2.1.8.3.6.9.4.5  \n" +
                " 1.2.5.8.9.4.3.6.7.8  "

        val board = BoardReader.readBoard(boardString)

        Assertions.assertEquals(81, board.candidatePatterns.size)
    }
}
