# Postfix Expression CSV Evaluator

## Description
This Java application reads a CSV file where each cell contains an expression written in postfix notation. It evaluates each cell, resolving basic arithmetic operations (`+`, `-`, `*`, `/`) and handling references to other cells within the same CSV file (e.g., A1, B2). The output is both printed to the console (STDOUT) and written to a new file named `output.csv`. If a cell contains an invalid expression, it outputs `#ERR` for that cell.

## Features
- Parses and evaluates cells containing postfix expressions.
- Supports basic arithmetic operations: `+`, `-`, `*`, `/`.
- Handles cell references (e.g., A1, B2).
- Outputs evaluated CSV to both STDOUT and a file named `output.csv`.
- Detects and handles errors such as invalid expressions and circular references.

## Requirements
- Java 8 or higher.

## Instructions to Run

1. **Compile the Code**:
   Open a terminal and navigate to the directory containing the Java files. Compile the code using the following command:

   ```bash
   javac Main.java
   ```

2. **Run the Program**:
   Run the program with the following command:

   ```bash
   java Main <input_file>
   ```

   Replace `<input_file>` with the path to the CSV file you want to evaluate.

   ### Example:
   ```bash
   java Main sample.csv
   ```

   The evaluated result will be printed to the console, and a file named `output.csv` will be generated in the current directory with the evaluated data.

## Example Input
Sample `input.csv`:
```
10,        1 3 +,      2  3  -
b1 b2 *,  a1,          b1 a2  /  c1 +
+,       1 2 3,      c3
```

## Example Output
The resulting `output.csv`:
```
10, 4, -1
40, 10, -0.9
#ERR, #ERR, #ERR
```

## Notes
- The input file should be in CSV format, with cells separated by commas.
- The application supports postfix notation for arithmetic expressions and single-letter column cell references (e.g., A1, B2).