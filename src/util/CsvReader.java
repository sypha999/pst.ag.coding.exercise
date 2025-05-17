package src.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class CsvReader {

    private static final char COMMA_DELIMITER = ',';
    private static final char QUOTE = '"';

    private CsvReader() {
    }

    /**
     * Reads a CSV file from the given path and returns the records as a list of string lists.
     * The first line (headers) is skipped.
     *
     * @param path Path to the CSV file
     * @return List of records, each record is a list of field values
     * @throws IOException if file reading fails
     */
    public static List<List<String>> read(Path path) throws IOException {
        List<List<String>> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // skip header line
                    continue;
                }
                records.add(parseLine(line));
            }
        }

        return records;
    }

    /**
     * Parses a CSV line into fields considering quoted fields with commas inside.
     *
     * @param line CSV line string
     * @return List of field values
     */
    private static List<String> parseLine(String line) {
        List<String> values = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentValue = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == QUOTE) {
                inQuotes = !inQuotes; // toggle quote state
            } else if (c == COMMA_DELIMITER && !inQuotes) {
                values.add(currentValue.toString());
                currentValue.setLength(0); // reset builder
            } else {
                currentValue.append(c);
            }
        }
        values.add(currentValue.toString()); // add last value
        return values;
    }
}
