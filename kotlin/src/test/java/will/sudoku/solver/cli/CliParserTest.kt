package will.sudoku.solver.cli

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName

@DisplayName("CliParser Tests")
class CliParserTest {

    @Test
    @DisplayName("Parse empty arguments")
    fun testParseEmptyArguments() {
        val config = CliParser.parse(emptyArray())
        assertThat(config.showHelp).isFalse()
        assertThat(config.showVersion).isFalse()
        assertThat(config.readStdin).isFalse()
        assertThat(config.hasInput()).isFalse()
        assertThat(config.format).isEqualTo(CliConfig.OutputFormat.DEFAULT)
    }

    @Test
    @DisplayName("Parse --help flag")
    fun testParseHelpFlag() {
        val config = CliParser.parse(arrayOf("--help"))
        assertThat(config.showHelp).isTrue()
        assertThat(config.showVersion).isFalse()
    }

    @Test
    @DisplayName("Parse -h flag (short help)")
    fun testParseShortHelpFlag() {
        val config = CliParser.parse(arrayOf("-h"))
        assertThat(config.showHelp).isTrue()
    }

    @Test
    @DisplayName("Parse --version flag")
    fun testParseVersionFlag() {
        val config = CliParser.parse(arrayOf("--version"))
        assertThat(config.showVersion).isTrue()
        assertThat(config.showHelp).isFalse()
    }

    @Test
    @DisplayName("Parse -v flag (short version)")
    fun testParseShortVersionFlag() {
        val config = CliParser.parse(arrayOf("-v"))
        assertThat(config.showVersion).isTrue()
    }

    @Test
    @DisplayName("Parse --format default")
    fun testParseFormatDefault() {
        val config = CliParser.parse(arrayOf("--format", "default"))
        assertThat(config.format).isEqualTo(CliConfig.OutputFormat.DEFAULT)
    }

    @Test
    @DisplayName("Parse --format pipe")
    fun testParseFormatPipe() {
        val config = CliParser.parse(arrayOf("--format", "pipe"))
        assertThat(config.format).isEqualTo(CliConfig.OutputFormat.PIPE)
    }

    @Test
    @DisplayName("Parse --format compact")
    fun testParseFormatCompact() {
        val config = CliParser.parse(arrayOf("--format", "compact"))
        assertThat(config.format).isEqualTo(CliConfig.OutputFormat.COMPACT)
    }

    @Test
    @DisplayName("Parse -f flag (short format)")
    fun testParseShortFormatFlag() {
        val config = CliParser.parse(arrayOf("-f", "pipe"))
        assertThat(config.format).isEqualTo(CliConfig.OutputFormat.PIPE)
    }

    @Test
    @DisplayName("Parse format with case insensitivity")
    fun testParseFormatCaseInsensitive() {
        val config = CliParser.parse(arrayOf("--format", "PIPE"))
        assertThat(config.format).isEqualTo(CliConfig.OutputFormat.PIPE)
    }

    @Test
    @DisplayName("Parse --string argument")
    fun testParseStringArgument() {
        val puzzleString = ".4.!3.8!1.."
        val config = CliParser.parse(arrayOf("--string", puzzleString))
        assertThat(config.stringInput).isEqualTo(puzzleString)
        assertThat(config.hasInput()).isTrue()
    }

    @Test
    @DisplayName("Parse stdin flag (-)")
    fun testParseStdinFlag() {
        val config = CliParser.parse(arrayOf("-"))
        assertThat(config.readStdin).isTrue()
        assertThat(config.hasInput()).isTrue()
    }

    @Test
    @DisplayName("Parse single file argument")
    fun testParseSingleFileArgument() {
        val config = CliParser.parse(arrayOf("puzzle.txt"))
        assertThat(config.inputFiles).isEqualTo(listOf("puzzle.txt"))
        assertThat(config.hasInput()).isTrue()
    }

    @Test
    @DisplayName("Parse multiple file arguments")
    fun testParseMultipleFileArguments() {
        val config = CliParser.parse(arrayOf("puzzle1.txt", "puzzle2.txt", "puzzle3.txt"))
        assertThat(config.inputFiles.size).isEqualTo(3)
        assertThat(config.inputFiles).isEqualTo(listOf("puzzle1.txt", "puzzle2.txt", "puzzle3.txt"))
    }

    @Test
    @DisplayName("Parse combined options")
    fun testParseCombinedOptions() {
        val config = CliParser.parse(arrayOf("--format", "pipe", "puzzle.txt"))
        assertThat(config.format).isEqualTo(CliConfig.OutputFormat.PIPE)
        assertThat(config.inputFiles).isEqualTo(listOf("puzzle.txt"))
    }

    @Test
    @DisplayName("Throw exception for --format without argument")
    fun testThrowExceptionForFormatWithoutArgument() {
        val exception = kotlin.runCatching {
            CliParser.parse(arrayOf("--format"))
        }.exceptionOrNull()

        assertThat(exception).isInstanceOf(CliException::class.java)
        assertThat(exception?.message).contains("requires an argument")
    }

    @Test
    @DisplayName("Throw exception for --string without argument")
    fun testThrowExceptionForStringWithoutArgument() {
        val exception = kotlin.runCatching {
            CliParser.parse(arrayOf("--string"))
        }.exceptionOrNull()

        assertThat(exception).isInstanceOf(CliException::class.java)
        assertThat(exception?.message).contains("requires an argument")
    }

    @Test
    @DisplayName("Throw exception for invalid format")
    fun testThrowExceptionForInvalidFormat() {
        val exception = kotlin.runCatching {
            CliParser.parse(arrayOf("--format", "invalid"))
        }.exceptionOrNull()

        assertThat(exception).isInstanceOf(CliException::class.java)
        assertThat(exception?.message).contains("Invalid format")
    }

    @Test
    @DisplayName("Throw exception for unknown option")
    fun testThrowExceptionForUnknownOption() {
        val exception = kotlin.runCatching {
            CliParser.parse(arrayOf("--unknown"))
        }.exceptionOrNull()

        assertThat(exception).isInstanceOf(CliException::class.java)
        assertThat(exception?.message).contains("Unknown option")
    }

    @Test
    @DisplayName("Throw exception for multiple --string arguments")
    fun testThrowExceptionForMultipleStringArguments() {
        val exception = kotlin.runCatching {
            CliParser.parse(arrayOf("--string", "puzzle1", "--string", "puzzle2"))
        }.exceptionOrNull()

        assertThat(exception).isInstanceOf(CliException::class.java)
        assertThat(exception?.message).contains("can only be specified once")
    }

    @Test
    @DisplayName("Throw exception for conflicting input sources (file and string)")
    fun testThrowExceptionForConflictingInputSourcesFileAndString() {
        val exception = kotlin.runCatching {
            CliParser.parse(arrayOf("puzzle.txt", "--string", "puzzle"))
        }.exceptionOrNull()

        assertThat(exception).isInstanceOf(CliException::class.java)
        assertThat(exception?.message).contains("Cannot specify multiple input sources")
    }

    @Test
    @DisplayName("Throw exception for conflicting input sources (file and stdin)")
    fun testThrowExceptionForConflictingInputSourcesFileAndStdin() {
        val exception = kotlin.runCatching {
            CliParser.parse(arrayOf("puzzle.txt", "-"))
        }.exceptionOrNull()

        assertThat(exception).isInstanceOf(CliException::class.java)
        assertThat(exception?.message).contains("Cannot specify multiple input sources")
    }

    @Test
    @DisplayName("Throw exception for conflicting input sources (string and stdin)")
    fun testThrowExceptionForConflictingInputSourcesStringAndStdin() {
        val exception = kotlin.runCatching {
            CliParser.parse(arrayOf("--string", "puzzle", "-"))
        }.exceptionOrNull()

        assertThat(exception).isInstanceOf(CliException::class.java)
        assertThat(exception?.message).contains("Cannot specify multiple input sources")
    }

    @Test
    @DisplayName("Help message is not empty")
    fun testHelpMessageIsNotEmpty() {
        val helpMessage = CliParser.getHelpMessage()
        assertThat(helpMessage).isNotEmpty()
        assertThat(helpMessage).contains("Usage:")
        assertThat(helpMessage).contains("Options:")
    }
}
