package will.sudoku.solver

/**
 * Example demonstrating metrics collection during Sudoku solving.
 *
 * This example shows how to:
 * - Solve a puzzle with metrics
 * - Display metrics in various formats
 * - Interpret the results to understand solver performance
 *
 * Run with: ./gradlew :kotlin:run
 */
object MetricsExample {

    @JvmStatic
    fun main(args: Array<String>) {
        // Sample medium-difficulty puzzle
        val puzzleString = """
                .4.!3.8!1..
                21.!.65!...
                6..!...!.7.
                ---!---!---
                9.3!.46!781
                1.4!829!5.6
                8.5!...!.2.
                ---!---!---
                4..!...!6.3
                ...!6.2!.47
                .8.!.3.!...
            """.trimIndent()

        println("=== Sudoku Solver Metrics Example ===")
        println()
        println("Puzzle to solve:")
        println(puzzleString)
        println()

        val board = BoardReader.readBoard(puzzleString)

        println("Initial board state:")
        println(board)
        println()

        // Solve with metrics
        println("Solving with metrics collection...")
        val solver = SolverWithMetrics()
        val result = solver.solveWithMetrics(board)

        println()

        if (result.solvedBoard != null) {
            println("✓ Puzzle solved successfully!")
            println()
            println("Solution:")
            println(result.solvedBoard)
            println()
        } else {
            println("✗ No solution found")
            println()
        }

        // Display metrics
        println("=== Solver Performance Metrics ===")
        println()
        println(result.metrics)
        println()

        // Display detailed metrics
        println("=== Detailed Performance Analysis ===")
        println()
        println(result.metrics.toDetailedString())

        // Display CSV for data analysis
        println("=== CSV Format (for data analysis) ===")
        println()
        println(result.metrics.toCSV())

        // Analysis and insights
        println("=== Performance Insights ===")
        println()
        analyzeMetrics(result.metrics)
    }

    /**
     * Analyze metrics and provide insights.
     */
    private fun analyzeMetrics(metrics: SolverMetrics) {
        val totalTimeMs = metrics.totalSolveTimeNanos / 1_000_000.0
        val avgTimePerPass = if (metrics.propagationPasses > 0) {
            metrics.totalSolveTimeNanos / metrics.propagationPasses.toDouble()
        } else 0.0

        println("Total Solve Time: ${"%.2f".format(totalTimeMs)} ms")

        if (metrics.backtrackingCount == 0) {
            println("✓ Solved by constraint propagation alone (no backtracking)")
            println("  This puzzle is suitable for the 'Easy' difficulty category")
        } else {
            println("Required ${metrics.backtrackingCount} backtracking attempts")
            if (metrics.backtrackingCount < 100) {
                println("  Low backtracking - puzzle is likely 'Easy' to 'Medium'")
            } else if (metrics.backtrackingCount < 1000) {
                println("  Moderate backtracking - puzzle is likely 'Medium' to 'Hard'")
            } else {
                println("  High backtracking - puzzle is likely 'Hard' to 'Expert'")
            }
        }

        val efficiency = if (metrics.propagationPasses > 0) {
            ((metrics.propagationPasses - metrics.backtrackingCount).toDouble() /
                metrics.propagationPasses * 100)
        } else 0.0

        println("Propagation Efficiency: ${"%.1f".format(efficiency)}%")
        println("  (Percentage of passes that made progress vs backtracking)")
        println()

        if (metrics.eliminatorMetrics.isNotEmpty()) {
            val mostActiveEliminator = metrics.eliminatorMetrics.entries
                .maxByOrNull { it.value.eliminations }

            if (mostActiveEliminator != null) {
                println("Most Active Eliminator: ${mostActiveEliminator.key}")
                println("  Eliminations: ${mostActiveEliminator.value.eliminations}")
                println("  Time: ${"%.2f".format(mostActiveEliminator.value.totalTimeNanos / 1_000_000.0)} ms")
            }

            val slowestEliminator = metrics.eliminatorMetrics.entries
                .maxByOrNull { it.value.totalTimeNanos }

            if (slowestEliminator != null && slowestEliminator != mostActiveEliminator) {
                println("Most Time-Consuming: ${slowestEliminator.key}")
                println("  Time: ${"%.2f".format(slowestEliminator.value.totalTimeNanos / 1_000_000.0)} ms")
            }
        }

        println()
        println("Recommendations:")
        if (metrics.backtrackingCount > 1000) {
            println("  - Consider adding more advanced elimination techniques")
            println("  - Hidden subset eliminator may help reduce backtracking")
        }
        if (metrics.maxRecursionDepth > 50) {
            println("  - Maximum recursion depth indicates complex puzzle")
            println("  - Metrics show solver explored deep branches")
        }
    }
}
