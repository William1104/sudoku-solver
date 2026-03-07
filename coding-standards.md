# Coding Standards

The project follows **Google Kotlin Style Guide** (https://kotlinlang.org/docs/coding-conventions.html).

## Key Principles

1. **Indentation**: 4 spaces (no tabs)
2. **Line Length**: Max 120 characters
3. **Naming**:
   - Classes: PascalCase (e.g., `Solver`)
   - Functions: camelCase, starting with lowercase (e.g., `solve`, `getBoard`)
   - Packages: lowercase (e.g., `will.sudoku.solver`)
   - Constants: UPPER_SNAKE_CASE (e.g., `MAX_CELLS`)
4. **Braces**: K&R style
5. **Imports**:
   - No wildcard imports
   - Sorted alphabetically
   - Group imports (third-party together, project separately)
6. **Functions**: Prefer public API functions over extensions on classes
7. **Comments**: No inline comments on multi-line statements
8. **KDoc**: All public APIs must have KDoc documentation

## File Organization

```
kotlin/src/main/java/will/sudoku/solver/
├── Main.kt                 # CLI entry point
├── cli/                     # CLI components
│   ├── CliConfig.kt
│   ├── CliParser.kt
│   ├── CliRunner.kt
│   ├── BoardFormatter.kt
│   └── CliException.kt
├── Board.kt                  # Board state & candidate pattern operations
├── Solver.kt                 # Main solver with backtracking
├── Settings.kt            # Configuration constants
├── Coord.kt               # Cell coordinates (row, col)
├── CoordGroup.kt          # Row/column/region groups
└── CandidateEliminator.kt # Interface for eliminators
```

## Code Style Enforcement

Detekt is configured to check code style against Google Kotlin Style Guide:
- Configuration: `.detekt/config.yml`
- GitHub Action: Runs on every push and PR
- Progressive mode: Issues are reported but don't fail the build
- Technical debt: Tracked via GitHub issues for gradual improvement

### Tolerated Violations

The following violations are tolerated and tracked as technical debt:
- **MagicNumber**: CLI parsers may use anonymous values for simple CLI argument parsing
- **Long lines**: Some CLI parsing code may exceed 120 chars
- **Complex methods**: Some solver algorithms may have high complexity (acceptable for backtracking algorithms)

These are tracked in GitHub issues and addressed incrementally.

## Code Style Resources

- **Google Kotlin Style Guide**: https://kotlinlang.org/docs/coding-conventions.html
- **Detekt Rules**: https://detekt.dev/docs/rules/
- **Kotlin API Documentation**: https://kotlinlang.org/api/latest/jvm/stdlib/
