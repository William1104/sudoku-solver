package will.sudoku.solver.cli

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.io.TempDir
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.nio.file.Path

@DisplayName("CliRunner Tests")
class CliRunnerTest {

    @Test
    @DisplayName("Run with showHelp returns 0")
    fun testRunWithShowHelpReturns0() {
        val config = CliConfig(showHelp = true)
        val exitCode = CliRunner.run(config)

        assertThat(exitCode).isEqualTo(0)
    }

    @Test
    @DisplayName("Run with showVersion returns 0")
    fun testRunWithShowVersionReturns0() {
        val config = CliConfig(showVersion = true)
        val exitCode = CliRunner.run(config)

        assertThat(exitCode).isEqualTo(0)
    }

    @Test
    @DisplayName("Run with no input returns 1")
    fun testRunWithNoInputReturns1() {
        val config = CliConfig()
        val exitCode = CliRunner.run(config)

        assertThat(exitCode).isEqualTo(1)
    }

    @Test
    @DisplayName("Run with valid string input returns 0")
    fun testRunWithValidStringInputReturns0() {
        val config = CliConfig(stringInput = ".4.!3.8!1..21.!.65!...6..!...!.7.---!---!---9.3!.46!7811.4!829!5.68.5!...!.2.---!---!---4..!...!6.3...!6.2!.47.8.!.3.!...")
        val exitCode = CliRunner.run(config)

        assertThat(exitCode).isEqualTo(0)
    }

    @Test
    @DisplayName("Run with invalid string input returns 1")
    fun testRunWithInvalidStringInputReturns1() {
        val config = CliConfig(stringInput = "invalid puzzle")
        val exitCode = CliRunner.run(config)

        assertThat(exitCode).isEqualTo(1)
    }

    @Test
    @DisplayName("Run with valid file returns 0")
    fun testRunWithValidFileReturns0(@TempDir tempDir: Path) {
        // Create a test puzzle file
        val puzzleFile = tempDir.resolve("puzzle.txt").toFile()
        puzzleFile.writeText(".4.!3.8!1..21.!.65!...6..!...!.7.---!---!---9.3!.46!7811.4!829!5.68.5!...!.2.---!---!---4..!...!6.3...!6.2!.47.8.!.3.!...")

        val config = CliConfig(inputFiles = listOf(puzzleFile.absolutePath))
        val exitCode = CliRunner.run(config)

        assertThat(exitCode).isEqualTo(0)
    }

    @Test
    @DisplayName("Run with non-existent file returns 1")
    fun testRunWithNonExistentFileReturns1() {
        val config = CliConfig(inputFiles = listOf("nonexistent.txt"))
        val exitCode = CliRunner.run(config)

        assertThat(exitCode).isEqualTo(1)
    }

    @Test
    @DisplayName("Run with multiple valid files returns 0")
    fun testRunWithMultipleValidFilesReturns0(@TempDir tempDir: Path) {
        // Create two test puzzle files
        val puzzle1 = tempDir.resolve("puzzle1.txt").toFile()
        puzzle1.writeText(".4.!3.8!1..21.!.65!...6..!...!.7.---!---!---9.3!.46!7811.4!829!5.68.5!...!.2.---!---!---4..!...!6.3...!6.2!.47.8.!.3.!...")

        val puzzle2 = tempDir.resolve("puzzle2.txt").toFile()
        puzzle2.writeText("53..7....6..195....98....6.8...6...34..8.3..17...2...6.6....28....419..5....8..79")

        val config = CliConfig(inputFiles = listOf(puzzle1.absolutePath, puzzle2.absolutePath))
        val exitCode = CliRunner.run(config)

        assertThat(exitCode).isEqualTo(0)
    }

    @Test
    @DisplayName("Run with one valid and one invalid file returns 1")
    fun testRunWithMixedFilesReturns1(@TempDir tempDir: Path) {
        // Create one valid puzzle file
        val puzzle1 = tempDir.resolve("puzzle1.txt").toFile()
        puzzle1.writeText(".4.!3.8!1..21.!.65!...6..!...!.7.---!---!---9.3!.46!7811.4!829!5.68.5!...!.2.---!---!---4..!...!6.3...!6.2!.47.8.!.3.!...")

        // Non-existent file
        val puzzle2 = "nonexistent.txt"

        val config = CliConfig(inputFiles = listOf(puzzle1.absolutePath, puzzle2))
        val exitCode = CliRunner.run(config)

        assertThat(exitCode).isEqualTo(1)
    }

    @Test
    @DisplayName("Run with pipe format")
    fun testRunWithPipeFormat() {
        val config = CliConfig(
            stringInput = ".4.!3.8!1..21.!.65!...6..!...!.7.---!---!---9.3!.46!7811.4!829!5.68.5!...!.2.---!---!---4..!...!6.3...!6.2!.47.8.!.3.!...",
            format = CliConfig.OutputFormat.PIPE
        )
        val exitCode = CliRunner.run(config)

        assertThat(exitCode).isEqualTo(0)
    }

    @Test
    @DisplayName("Run with compact format")
    fun testRunWithCompactFormat() {
        val config = CliConfig(
            stringInput = ".4.!3.8!1..21.!.65!...6..!...!.7.---!---!---9.3!.46!7811.4!829!5.68.5!...!.2.---!---!---4..!...!6.3...!6.2!.47.8.!.3.!...",
            format = CliConfig.OutputFormat.COMPACT
        )
        val exitCode = CliRunner.run(config)

        assertThat(exitCode).isEqualTo(0)
    }

    @Test
    @DisplayName("Run with unsolvable puzzle returns 1")
    fun testRunWithUnsolvablePuzzleReturns1() {
        // Create an unsolvable puzzle (duplicate values in first row)
        val config = CliConfig(stringInput = "11...........0...........0...........0...........0...........0...........0...........0...........0...........0")
        val exitCode = CliRunner.run(config)

        assertThat(exitCode).isEqualTo(1)
    }
}
