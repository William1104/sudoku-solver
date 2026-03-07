package will.sudoku.solver.cli

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import will.sudoku.solver.Board

@DisplayName("BoardFormatter Tests")
class BoardFormatterTest {

    @Test
    @DisplayName("Format board in default format")
    fun testFormatDefault() {
        // Create a simple solved board for testing
        val board = createTestBoard()
        val formatted = BoardFormatter.format(board, CliConfig.OutputFormat.DEFAULT)

        // Default format should contain separators
        assertThat(formatted).contains("!")
        assertThat(formatted).contains("-")
    }

    @Test
    @DisplayName("Format board in pipe format")
    fun testFormatPipe() {
        val board = createTestBoard()
        val formatted = BoardFormatter.format(board, CliConfig.OutputFormat.PIPE)

        // Pipe format should not contain visual separators
        // Each line should be 9 characters (no separators)
        val lines = formatted.lines().filter { it.isNotEmpty() }
        assertThat(lines.size).isEqualTo(9)

        // Each line should be exactly 9 characters
        lines.forEach { line ->
            assertThat(line.length).isEqualTo(9)
        }

        // Pipe format should not contain visual separators
        assertThat(formatted).doesNotContain("!")
        assertThat(formatted).doesNotContain("---")
    }

    @Test
    @DisplayName("Format board in compact format")
    fun testFormatCompact() {
        val board = createTestBoard()
        val formatted = BoardFormatter.format(board, CliConfig.OutputFormat.COMPACT)

        // Compact format should be a single line of 81 characters
        val lines = formatted.lines().filter { it.isNotEmpty() }
        assertThat(lines.size).isEqualTo(1)
        assertThat(lines[0].length).isEqualTo(81)

        // Compact format should not contain visual separators
        assertThat(formatted).doesNotContain("!")
        assertThat(formatted).doesNotContain("-")
        assertThat(formatted).doesNotContain("\n")
    }

    @Test
    @DisplayName("Pipe format contains correct values")
    fun testPipeFormatContainsCorrectValues() {
        val board = createTestBoard()
        val formatted = BoardFormatter.format(board, CliConfig.OutputFormat.PIPE)
        val lines = formatted.lines().filter { it.isNotEmpty() }

        // Check first row: 123456789
        assertThat(lines[0]).isEqualTo("123456789")

        // Check second row: 123456789 (all rows are the same in test board)
        assertThat(lines[1]).isEqualTo("123456789")
    }

    @Test
    @DisplayName("Compact format contains correct values")
    fun testCompactFormatContainsCorrectValues() {
        val board = createTestBoard()
        val formatted = BoardFormatter.format(board, CliConfig.OutputFormat.COMPACT)
        val line = formatted.trim()

        // Compact format should be 9 rows of 123456789
        val expected = "123456789".repeat(9)
        assertThat(line).isEqualTo(expected)
    }

    @Test
    @DisplayName("Default format contains line separators")
    fun testDefaultFormatContainsLineSeparators() {
        val board = createTestBoard()
        val formatted = BoardFormatter.format(board, CliConfig.OutputFormat.DEFAULT)

        // Default format should have row separators
        assertThat(formatted).contains("---")
    }

    /**
     * Creates a test board with all cells filled with values 1-9.
     * Each row is 123456789.
     */
    private fun createTestBoard(): Board {
        val values = IntArray(81) { index ->
            // Each cell has value 1-9 based on column (all rows are the same)
            (index % 9) + 1
        }
        return Board(values)
    }
}
