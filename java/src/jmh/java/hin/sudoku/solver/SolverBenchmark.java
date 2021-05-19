package hin.sudoku.solver;

import com.google.common.collect.ImmutableMap;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SolverBenchmark {
    private static final Map<String, int[][]> GRIDS = ImmutableMap.<String, int[][]>builder()
            .put("g1", new int[][]{
                    {0, 4, 0, 3, 0, 8, 1, 0, 0},
                    {2, 1, 0, 0, 6, 5, 0, 0, 0},
                    {6, 0, 0, 0, 0, 0, 0, 7, 0},
                    {9, 0, 3, 0, 4, 6, 7, 8, 1},
                    {1, 0, 4, 8, 2, 9, 5, 0, 6},
                    {8, 0, 5, 0, 0, 0, 0, 2, 0},
                    {4, 0, 0, 0, 0, 0, 6, 0, 3},
                    {0, 0, 0, 6, 0, 2, 0, 4, 7},
                    {0, 8, 0, 0, 3, 0, 0, 0, 0}
            })
            .put("g2", new int[][]{
                    {1, 0, 0, 4, 9, 6, 0, 0, 2},
                    {3, 0, 6, 0, 1, 0, 7, 0, 0},
                    {0, 8, 0, 0, 0, 3, 1, 0, 6},
                    {0, 0, 5, 0, 6, 0, 0, 0, 8},
                    {0, 6, 3, 0, 8, 5, 0, 0, 9},
                    {0, 0, 0, 3, 0, 4, 5, 0, 1},
                    {6, 0, 2, 0, 0, 0, 9, 0, 4},
                    {8, 0, 0, 6, 0, 9, 0, 5, 0},
                    {5, 0, 9, 8, 2, 0, 6, 0, 0}
            })
            .put("g3", new int[][]{
                    {0, 6, 0, 0, 0, 5, 0, 0, 0},
                    {0, 7, 0, 0, 0, 0, 0, 0, 1},
                    {0, 0, 0, 0, 6, 3, 4, 0, 0},
                    {0, 0, 3, 0, 8, 0, 0, 0, 0},
                    {2, 1, 0, 0, 9, 0, 0, 0, 5},
                    {4, 0, 0, 0, 0, 7, 8, 0, 0},
                    {0, 0, 1, 6, 0, 0, 0, 8, 4},
                    {0, 0, 0, 0, 0, 0, 0, 5, 0},
                    {8, 0, 0, 0, 4, 0, 6, 1, 0}
            })
            .put("g4", new int[][]{
                    {7, 0, 0, 0, 4, 0, 2, 0, 0},
                    {0, 0, 0, 5, 2, 0, 0, 0, 6},
                    {0, 0, 0, 0, 0, 0, 5, 0, 0},
                    {0, 7, 0, 0, 0, 0, 9, 6, 0},
                    {0, 6, 0, 0, 0, 0, 0, 8, 0},
                    {4, 2, 5, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 9, 0, 3, 1},
                    {0, 0, 4, 0, 0, 7, 0, 0, 0},
                    {1, 0, 0, 6, 0, 0, 0, 0, 0}
            })
            .build();

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
    public void solve(final Blackhole blackhole, BenchmarkState state) {
        final var solver = new Solver();
        blackhole.consume(solver.solve(3, state.grid));
    }
    
    @State(Scope.Benchmark)
    public static class BenchmarkState {
        @Param({"g1", "g2", "g3", "g4"})
        public String gridName;

        public int[][] grid;

        @Setup(Level.Trial)
        public void setUp() {
            grid = GRIDS.get(gridName);
        }
    }
}
