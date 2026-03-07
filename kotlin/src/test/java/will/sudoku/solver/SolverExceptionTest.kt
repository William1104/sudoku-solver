package will.sudoku.solver

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class SolverExceptionTest {

    @Test
    fun `SolverException has correct message and exit code`() {
        val exception = SolverException("Cannot solve puzzle")

        assertThat(exception.message).isEqualTo("Cannot solve puzzle")
        assertThat(exception.exitCode).isEqualTo(1)
        assertThat(exception.cause).isNull()
    }

    @Test
    fun `SolverException can be created with exit code`() {
        val exitCode = 2
        val exception = SolverException("Timeout exceeded", exitCode = exitCode)

        assertThat(exception.message).isEqualTo("Timeout exceeded")
        assertThat(exception.exitCode).isEqualTo(2)
    }

    @Test
    fun `SolverException is unchecked exception`() {
        val exceptionClass = SolverException::class.java

        // Unchecked exceptions extend from RuntimeException
        assertThat(RuntimeException::class.java.isAssignableFrom(SolverException::class.java))
            .isTrue()
    }
}
