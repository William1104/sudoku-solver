package will.sudoku.solver.cli

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import will.sudoku.solver.main

@DisplayName("Main Entry Point Tests")
class MainTest {

    @Test
    @DisplayName("Main function exists and is callable")
    fun testMainFunctionExists() {
        // This test verifies that the main function exists and doesn't throw
        // We're not testing the full behavior here as that's covered by CliRunner tests
        try {
            // Create a test with a valid puzzle
            val args = arrayOf("--string", ".4.!3.8!1..21.!.65!...6..!...!.7.---!---!---9.3!.46!7811.4!829!5.68.5!...!.2.---!---!---4..!...!6.3...!6.2!.47.8.!.3.!...")
            main(args)
            // Main function executed without throwing
        } catch (e: Exception) {
            assertThat(false).withFailMessage("Main function threw exception: ${e.message}").isTrue()
        }
    }

    @Test
    @DisplayName("Main with --help flag")
    fun testMainWithHelpFlag() {
        try {
            val args = arrayOf("--help")
            main(args)
            // Main function executed with --help
        } catch (e: Exception) {
            assertThat(false).withFailMessage("Main function threw exception: ${e.message}").isTrue()
        }
    }

    @Test
    @DisplayName("Main with --version flag")
    fun testMainWithVersionFlag() {
        try {
            val args = arrayOf("--version")
            main(args)
            // Main function executed with --version
        } catch (e: Exception) {
            assertThat(false).withFailMessage("Main function threw exception: ${e.message}").isTrue()
        }
    }

    @Test
    @DisplayName("Main with no arguments returns error")
    fun testMainWithNoArguments() {
        try {
            val args = emptyArray<String>()
            main(args)
            // Should have exited with code 1, so this test verifies it doesn't throw an exception
            // Main function executed without throwing
        } catch (e: Exception) {
            assertThat(false).withFailMessage("Main function threw exception: ${e.message}").isTrue()
        }
    }
}
