package hin.sudoku.solver;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.infra.Blackhole;
import will.sudoku.solver.BoardReader;
import will.sudoku.solver.Solver;

import java.util.concurrent.TimeUnit;

public class SolverBenchmark {
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void simplePuzzle(Blackhole bh) {
        Solver solver = new Solver();
        bh.consume(solver.solve(BoardReader.readBoard(
                ".4.!3.8!1.." +
                        "21.!.65!..." +
                        "6..!...!.7." +
                        "---!---!---" +
                        "9.3!.46!781" +
                        "1.4!829!5.6" +
                        "8.5!...!.2." +
                        "---!---!---" +
                        "4..!...!6.3" +
                        "...!6.2!.47" +
                        ".8.!.3.!...")));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void hardPuzzle(Blackhole bh) {
        Solver solver = new Solver();
        bh.consume(solver.solve(BoardReader.readBoard(
                "7..!.4.!2.." +
                        "...!52.!..6" +
                        "...!...!5.." +
                        "---!---!---" +
                        ".7.!...!96." +
                        ".6.!...!.8." +
                        "425!...!..." +
                        "---!---!---" +
                        "...!..9!.31" +
                        "..4!..7!..." +
                        "1..!6..!...")));
    }
}
