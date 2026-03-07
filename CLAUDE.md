# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a high-performance Sudoku solver implemented in Kotlin. The solver uses bitmask-based candidate representation and multiple constraint propagation strategies combined with backtracking.

## Build System

Single-module Gradle project:
- `kotlin` - Kotlin implementation

### Common Commands

```bash
# Build the project
./gradlew build

# Clean build artifacts
./gradlew clean

# Run all tests
./gradlew test

# Run single test class
./gradlew :kotlin:test --tests will.sudoku.solver.SolverTest

# Run JMH benchmarks (manual trigger only)
./gradlew :kotlin:jmh
```

## Architecture

### Core Data Model

The solver centers around the `Board` class which maintains state as a 81-element `IntArray` of candidate patterns:

- Each cell has a 9-bit integer representing possible values
- Bit `i` (0-indexed) corresponds to value `i+1`
- `Board.masks[]` provides bitmask values: `0b000000001` for value 1, `0b000000010` for value 2, etc.
- Confirmed cell = single bit set; unresolved cell = multiple bits set
- Empty/unknown cell = all bits set (`(1 shl size) - 1`)

Example: `candidatePattern = 0b000001010` means values 2 and 4 are candidates.

### Coordinate System

- `Coord(row: Int, col: Int)` - Zero-indexed (0-8), with computed `index` property
- `Coord.all` - Pre-computed array of all 81 coordinates
- `CoordGroup` - Represents rows, columns, and 3x3 regions
- Each cell belongs to 3 groups: `CoordGroup.verticalOf()`, `horizontalOf()`, `regionOf()`

### Solver Algorithm

The `Solver` class implements backtracking with constraint propagation:

1. Apply all eliminators (`Settings.eliminators`) until no changes
2. Select unresolved cell with minimum remaining candidates (MRV heuristic via `unresolvedCoord()`)
3. Try each candidate value recursively
4. After placing value, reapply eliminators before next recursion

### Eliminator Pattern

All eliminators implement `CandidateEliminator` interface:
```kotlin
interface CandidateEliminator {
    fun eliminate(board: Board): Boolean  // Returns true if any changes made
}
```

The three eliminators in `Settings.eliminators`:

1. `SimpleCandidateEliminator` - Removes confirmed values from peer cells
2. `GroupCandidateEliminator` - Detects naked pairs/triples (naked subsets)
3. `ExclusionCandidateEliminator` - Detects hidden singles; takes `shortCircuitThreshold` parameter to skip groups with too many known values

### Board Format

Text format with optional visual separators:
- `.` or `0` - Empty/unknown cell
- `1-9` - Confirmed value
- `!` - Column separator (every 3 columns)
- `-` - Row separator (every 3 rows)

Example:
```
.4.!3.8!1..
21.!.65!...
6..!...!.7.
---!---!---
```

Parsed by `BoardReader.readBoard()` which accepts `String`, `File`, or `InputStream`.

### Test Structure

Tests use parameterized board files in `kotlin/src/test/resources/solver/`:
- `<name>.question` - The unsolved puzzle
- `<name>.solution` - The expected solution

`SolverTest` automatically discovers and pairs these files using reflection.

### Benchmarking

JMH benchmarks are in `kotlin/src/jmh/`. They test performance across:
- Multiple puzzle files (g1-g4 from www.sudokuweb.org)
- Different `shortCircuitThreshold` values (0, 3, 6, 9)

Benchmarks run only via manual GitHub Actions workflow dispatch.

## Configuration

Global settings in `Settings` object:
- `size = 9` - Board dimensions
- `regionSize = 3` - 3x3 subgrids
- `symbols` - Display characters (`.`, `1`-`9`)
- `eliminators` - Ordered list of eliminators applied during solving

To add a new eliminator:
1. Implement `CandidateEliminator` interface
2. Add instance to `Settings.eliminators` list
3. Configure any parameters in `Settings`

## Module Structure

```
kotlin/src/main/java/will/sudoku/solver/
├── Board.kt                  # Board state & candidate pattern operations
├── Solver.kt                 # Main solver with backtracking
├── Settings.kt               # Global configuration & eliminator instances
├── Coord.kt                  # Cell coordinates (row, col) with computed index
├── CoordGroup.kt             # Row/column/region groups
├── CandidateEliminator.kt    # Interface for eliminators
├── SimpleCandidateEliminator.kt
├── GroupCandidateEliminator.kt
├── ExclusionCandidateEliminator.kt
└── BoardReader.kt            # Parse boards from files/strings
```
