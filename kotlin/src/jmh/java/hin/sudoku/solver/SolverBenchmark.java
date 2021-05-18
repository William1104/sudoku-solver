package hin.sudoku.solver;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

public class SolverBenchmark {
	public static void main(String[] args) throws RunnerException {
		final var opt = new OptionsBuilder()
				.include(SolverBenchmark.class.getSimpleName())
				.forks(1)
				.build();
		new Runner(opt).run();
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MICROSECONDS)
	public void simplePuzzle() {
		final int[][] grid = {
				{0, 4, 0, 3, 0, 8, 1, 0, 0},
				{2, 1, 0, 0, 6, 5, 0, 0, 0},
				{6, 0, 0, 0, 0, 0, 0, 7, 0},
				{9, 0, 3, 0, 4, 6, 7, 8, 1},
				{1, 0, 4, 8, 2, 9, 5, 0, 6},
				{8, 0, 5, 0, 0, 0, 0, 2, 0},
				{4, 0, 0, 0, 0, 0, 6, 0, 3},
				{0, 0, 0, 6, 0, 2, 0, 4, 7},
				{0, 8, 0, 0, 3, 0, 0, 0, 0}
		};
		final var solver = new Solver();
		solver.solve(3, grid);
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MICROSECONDS)
	public void hardPuzzle() {
		final int[][] grid = {
				{7, 0, 0, 0, 4, 0, 2, 0, 0},
				{0, 0, 0, 5, 2, 0, 0, 0, 6},
				{0, 0, 0, 0, 0, 0, 5, 0, 0},
				{0, 7, 0, 0, 0, 0, 9, 6, 0},
				{0, 6, 0, 0, 0, 0, 0, 8, 0},
				{4, 2, 5, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 9, 0, 3, 1},
				{0, 0, 4, 0, 0, 7, 0, 0, 0},
				{1, 0, 0, 6, 0, 0, 0, 0, 0}
		};
		final var solver = new Solver();
		solver.solve(3, grid);
	}
}
