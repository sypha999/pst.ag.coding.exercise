package src.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class DateFormatter {

    private static final DateTimeFormatter XML_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private DateFormatter() {
    }

    /**
     * Parses a date string in MM/dd/yyyy format to a LocalDate.
     *
     * @param date the date string to parse
     * @return the parsed LocalDate
     * @throws DateTimeParseException if the input string cannot be parsed
     */
    public static LocalDate formatDate(String date) {
        if (date == null || date.isBlank()) {
            throw new IllegalArgumentException("Date string cannot be null or empty");
        }
        return LocalDate.parse(date.trim(), XML_FORMAT);
    }
}
