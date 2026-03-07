package will.sudoku.solver.cli

/**
 * Parser for command-line arguments.
 *
 * This object parses command-line arguments and returns a [CliConfig] object.
 * It handles help, version, format options, and input sources (files, stdin, string).
 */
object CliParser {

    /**
     * Parses command-line arguments into a configuration object.
     *
     * Supported options:
     * - `--help`, `-h`: Display help message
     * - `--version`, `-v`: Display version information
     * - `--format <type>`, `-f <type>`: Set output format (default, pipe, compact)
     * - `--string <puzzle>`: Solve puzzle from string argument
     * - `-`: Read puzzle from stdin
     * - `<file>...`: Read puzzle(s) from file(s)
     *
     * @param args Command-line arguments
     * @return Parsed configuration
     * @throws CliException if arguments are invalid
     */
    fun parse(args: Array<String>): CliConfig {
        var showHelp = false
        var showVersion = false
        var format = CliConfig.OutputFormat.DEFAULT
        var stringInput: String? = null
        var readStdin = false
        val inputFiles = mutableListOf<String>()

        var i = 0
        while (i < args.size) {
            when (val arg = args[i]) {
                "--help", "-h" -> {
                    showHelp = true
                }

                "--version", "-v" -> {
                    showVersion = true
                }

                "--format", "-f" -> {
                    if (i + 1 >= args.size) {
                        throw CliException("Option '$arg' requires an argument")
                    }
                    format = parseFormat(args[++i])
                }

                "--string" -> {
                    if (i + 1 >= args.size) {
                        throw CliException("Option '$arg' requires an argument")
                    }
                    if (stringInput != null) {
                        throw CliException("Option '$arg' can only be specified once")
                    }
                    stringInput = args[++i]
                }

                "-" -> {
                    readStdin = true
                }

                else -> {
                    // Check if it's a file
                    if (arg.startsWith("-")) {
                        throw CliException("Unknown option: $arg")
                    }
                    inputFiles.add(arg)
                }
            }
            i++
        }

        // Validate conflicting options
        val inputSources = listOfNotNull(
            inputFiles.takeIf { it.isNotEmpty() },
            stringInput?.let { listOf(it) },
            "-".takeIf { readStdin }
        )

        if (inputSources.size > 1) {
            throw CliException(
                "Cannot specify multiple input sources. Choose one: file(s), --string, or stdin (-)"
            )
        }

        return CliConfig(
            showHelp = showHelp,
            showVersion = showVersion,
            format = format,
            inputFiles = inputFiles.toList(),
            stringInput = stringInput,
            readStdin = readStdin
        )
    }

    /**
     * Parses format string into OutputFormat enum.
     *
     * @param formatStr Format string
     * @return Parsed OutputFormat
     * @throws CliException if format is invalid
     */
    private fun parseFormat(formatStr: String): CliConfig.OutputFormat {
        return when (formatStr.lowercase()) {
            "default" -> CliConfig.OutputFormat.DEFAULT
            "pipe" -> CliConfig.OutputFormat.PIPE
            "compact" -> CliConfig.OutputFormat.COMPACT
            else -> throw CliException(
                "Invalid format: $formatStr. Valid formats: default, pipe, compact"
            )
        }
    }

    /**
     * Generates help message.
     *
     * @return Help message string
     */
    fun getHelpMessage(): String {
        return buildString {
            appendLine("Sudoku Solver - Solve Sudoku puzzles from command line")
            appendLine()
            appendLine("Usage:")
            appendLine("  ./gradlew :kotlin:run --args=\"[OPTIONS] [FILE...]\"")
            appendLine()
            appendLine("Options:")
            appendLine("  -h, --help          Display this help message")
            appendLine("  -v, --version       Display version information")
            appendLine("  -f, --format TYPE   Set output format (default, pipe, compact)")
            appendLine("  --string PUZZLE     Solve puzzle from string argument")
            appendLine("  -                   Read puzzle from stdin")
            appendLine()
            appendLine("Input Methods:")
            appendLine("  FILE...              Read puzzle(s) from file(s)")
            appendLine("  --string PUZZLE      Solve puzzle from string argument")
            appendLine("  -                    Read puzzle from stdin")
            appendLine()
            appendLine("Output Formats:")
            appendLine("  default              Visual format with separators (default)")
            appendLine("  pipe                 Each row as a pipe-separated line")
            appendLine("  compact              Single line of 81 characters")
            appendLine()
            appendLine("Examples:")
            appendLine("  ./gradlew :kotlin:run --args=\"puzzle.txt\"")
            appendLine("  ./gradlew :kotlin:run --args=\"puzzle1.txt puzzle2.txt\"")
            appendLine("  ./gradlew :kotlin:run --args=\"--format pipe puzzle.txt\"")
            appendLine("  ./gradlew :kotlin:run --args=\"--string '.4.!3.8!1..'\"")
            appendLine("  cat puzzle.txt | ./gradlew :kotlin:run --args=\"-\"")
            appendLine()
            appendLine("Puzzle Format:")
            appendLine("  . or 0              Empty cell")
            appendLine("  1-9                 Confirmed value")
            appendLine("  !                   Column separator (optional, every 3 columns)")
            appendLine("  -                   Row separator (optional, every 3 rows)")
        }
    }
}
