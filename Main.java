import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        // Check if an input file is provided
        if (args.length != 1) {
            System.err.println("Usage: java Main <input_file>");
            System.exit(1);
        }

        String inputFile = args[0];
        List<List<String>> csvData = readCSV(inputFile); // Read input CSV file
        List<List<String>> evaluatedData = evaluateCSV(csvData); // Evaluate cells

        // Output results to STDOUT
        printCSV(evaluatedData);

        // Write output to fixed file name
        String outputFileName = "output.csv";
        writeCSV(evaluatedData, outputFileName); // Write evaluated data to output file
        // System.out.println("Output written to: " + outputFileName);
    }

    // Method to read CSV file
    private static List<List<String>> readCSV(String inputFile) {
        List<List<String>> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> row = Arrays.asList(line.split(","));
                data.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    // Method to evaluate the CSV
    private static List<List<String>> evaluateCSV(List<List<String>> csvData) {
        List<List<String>> result = new ArrayList<>();
        Map<String, Double> cellValues = new HashMap<>();
        Set<String> evaluatingCells = new HashSet<>();

        for (int i = 0; i < csvData.size(); i++) {
            List<String> row = new ArrayList<>();
            for (int j = 0; j < csvData.get(i).size(); j++) {
                String cell = csvData.get(i).get(j).trim();
                try {
                    double value = evaluateCell(cell, cellValues, csvData, evaluatingCells);
                    row.add(formatValue(value)); // Add evaluated value
                } catch (Exception e) {
                    row.add("#ERR"); // Add error if evaluation fails
                }
            }
            result.add(row);
        }
        return result;
    }

    // Evaluate individual cells in the CSV
    public static double evaluateCell(String cell, Map<String, Double> cellValues, List<List<String>> csvData, Set<String> evaluatingCells) {
        if (cellValues.containsKey(cell)) {
            return cellValues.get(cell);
        }

        if (evaluatingCells.contains(cell)) {
            throw new IllegalArgumentException("Circular reference detected");
        }

        evaluatingCells.add(cell);
        Stack<Double> stack = new Stack<>();
        String[] tokens = cell.split("\\s+");

        for (String token : tokens) {
            if (isNumeric(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isCellReference(token)) {
                double value = evaluateCell(getCellValue(token, csvData), cellValues, csvData, evaluatingCells);
                stack.push(value);
            } else {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Not enough operands for operator: " + token);
                }
                double b = stack.pop();
                double a = stack.pop();
                switch (token) {
                    case "+":
                        stack.push(a + b);
                        break;
                    case "-":
                        stack.push(a - b);
                        break;
                    case "*":
                        stack.push(a * b);
                        break;
                    case "/":
                        if (b == 0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        stack.push(a / b);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid operator: " + token);
                }
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid postfix expression: " + cell);
        }

        double result = stack.pop();
        cellValues.put(cell, result);
        evaluatingCells.remove(cell);
        return result;
    }

    // Utility to check if a string is a numeric value
    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Utility to check if a string is a cell reference
    private static boolean isCellReference(String str) {
        return str.matches("[a-zA-Z]+[0-9]+");
    }

    // Retrieve the cell value based on its reference (e.g., A1, B2)
    private static String getCellValue(String reference, List<List<String>> csvData) {
        int row = Integer.parseInt(reference.replaceAll("[^0-9]", "")) - 1;
        int col = reference.replaceAll("[^a-zA-Z]", "").toUpperCase().charAt(0) - 'A';
        return csvData.get(row).get(col).trim();
    }

    // Format the evaluated value for output (e.g., 4.0 -> 4)
    private static String formatValue(double value) {
        if (value == (long) value) {
            return String.format("%d", (long) value);
        } else {
            return String.format("%.1f", value);
        }
    }

    // Print the CSV result to STDOUT
    private static void printCSV(List<List<String>> csvData) {
        for (List<String> row : csvData) {
            System.out.println(String.join(", ", row));
        }
    }

    // Write the CSV result to a fixed output file
    private static void writeCSV(List<List<String>> csvData, String outputFileName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(outputFileName))) {
            for (List<String> row : csvData) {
                pw.println(String.join(", ", row));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
