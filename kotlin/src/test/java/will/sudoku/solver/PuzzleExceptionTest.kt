package will.sudoku.solver

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PuzzleExceptionTest {

    @Test
    fun `PuzzleException has correct message and default exit code`() {
        val exception = PuzzleException("Invalid puzzle format")

        assertThat(exception.message).isEqualTo("Invalid puzzle format")
        assertThat(exception.suggestion).isNull()
        assertThat(exception.exitCode).isEqualTo(2) // Default exit code
    }

    @Test
    fun `PuzzleException can have suggestion set`() {
        val suggestion = "Check separator lines and ensure 9x9 grid"
        val exception = PuzzleException("Invalid puzzle format")
        exception.suggestion = suggestion

        assertThat(exception.message).isEqualTo("Invalid puzzle format")
        assertThat(exception.suggestion).isEqualTo(suggestion)
    }

    @Test
    fun `PuzzleException can have exit code set`() {
        val exitCode = 3
        val exception = PuzzleException("Invalid puzzle format")
        exception.exitCode = exitCode

        assertThat(exception.message).isEqualTo("Invalid puzzle format")
        assertThat(exception.exitCode).isEqualTo(3)
    }

    @Test
    fun `PuzzleException can wrap cause`() {
        val cause = RuntimeException("Original error")
        val exception = PuzzleException("Puzzle error", cause = cause)

        assertThat(exception.message).isEqualTo("Puzzle error")
        assertThat(exception.cause).isEqualTo(cause)
        assertThat(exception.exitCode).isEqualTo(2)
    }

    @Test
    fun `PuzzleException extends RuntimeException`() {
        val exception = PuzzleException("Test error")

        assertThat(exception).isInstanceOf(RuntimeException::class.java)
        assertThat(exception.message).isEqualTo("Test error")
    }

    @Test
    fun `PuzzleException is unchecked exception`() {
        val exceptionClass = PuzzleException::class.java

        assertThat(exceptionClass.modifiers).isEmpty()
    }
}
