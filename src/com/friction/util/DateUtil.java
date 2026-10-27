package com.friction.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/** Converts dates to and from the DD-MM-YYYY format shown in the CLI. */
public class DateUtil {
    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);

    public static String format(LocalDate date) {
        return date.format(FORMAT);
    }

    /** Returns null when the text is not a valid date. */
    public static LocalDate parse(String text) {
        try {
            return LocalDate.parse(text.trim(), FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
