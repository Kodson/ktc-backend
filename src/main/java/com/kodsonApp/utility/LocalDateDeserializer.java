package com.kodsonApp.utility;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String date = p.getText();
        DateTimeParseException lastException = null;
        String[] patterns = {
            "yyyy-MM-dd", // ISO
            "MMM dd, yyyy", // Feb 14, 2025
            "MM/dd/yyyy", // 09/30/2025
            "dd/MM/yyyy", // 30/09/2025
            "MM-dd-yyyy", // 09-30-2025
            "dd-MM-yyyy"  // 30-09-2025
        };
        for (String pattern : patterns) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                lastException = e;
            }
        }
        throw new IOException("Invalid date format for LocalDate: " + date, lastException);
    }
}
