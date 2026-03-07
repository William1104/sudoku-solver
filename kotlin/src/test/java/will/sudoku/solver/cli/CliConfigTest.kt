package will.sudoku.solver.cli

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName

@DisplayName("CliConfig Tests")
class CliConfigTest {

    @Test
    @DisplayName("Default config has no input")
    fun testDefaultConfigHasNoInput() {
        val config = CliConfig()
        assertThat(config.hasInput()).isFalse()
    }

    @Test
    @DisplayName("Config with input files has input")
    fun testConfigWithInputFilesHasInput() {
        val config = CliConfig(inputFiles = listOf("puzzle.txt"))
        assertThat(config.hasInput()).isTrue()
    }

    @Test
    @DisplayName("Config with string input has input")
    fun testConfigWithStringInputHasInput() {
        val config = CliConfig(stringInput = ".4.3.81..")
        assertThat(config.hasInput()).isTrue()
    }

    @Test
    @DisplayName("Config with stdin flag has input")
    fun testConfigWithStdinFlagHasInput() {
        val config = CliConfig(readStdin = true)
        assertThat(config.hasInput()).isTrue()
    }

    @Test
    @DisplayName("Config with showHelp flag")
    fun testConfigWithShowHelp() {
        val config = CliConfig(showHelp = true)
        assertThat(config.showHelp).isTrue()
    }

    @Test
    @DisplayName("Config with showVersion flag")
    fun testConfigWithShowVersion() {
        val config = CliConfig(showVersion = true)
        assertThat(config.showVersion).isTrue()
    }

    @Test
    @DisplayName("Config with default format")
    fun testConfigWithDefaultFormat() {
        val config = CliConfig(format = CliConfig.OutputFormat.DEFAULT)
        assertThat(config.format).isEqualTo(CliConfig.OutputFormat.DEFAULT)
    }

    @Test
    @DisplayName("Config with pipe format")
    fun testConfigWithPipeFormat() {
        val config = CliConfig(format = CliConfig.OutputFormat.PIPE)
        assertThat(config.format).isEqualTo(CliConfig.OutputFormat.PIPE)
    }

    @Test
    @DisplayName("Config with compact format")
    fun testConfigWithCompactFormat() {
        val config = CliConfig(format = CliConfig.OutputFormat.COMPACT)
        assertThat(config.format).isEqualTo(CliConfig.OutputFormat.COMPACT)
    }

    @Test
    @DisplayName("Config with multiple input files")
    fun testConfigWithMultipleInputFiles() {
        val config = CliConfig(inputFiles = listOf("puzzle1.txt", "puzzle2.txt", "puzzle3.txt"))
        assertThat(config.inputFiles.size).isEqualTo(3)
        assertThat(config.hasInput()).isTrue()
    }

    @Test
    @DisplayName("VERSION constant is accessible")
    fun testVersionConstant() {
        val version = CliConfig.VERSION
        assertThat(version).isEqualTo("0.1.0")
    }
}
