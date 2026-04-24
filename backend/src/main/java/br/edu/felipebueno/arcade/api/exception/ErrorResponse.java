package br.edu.felipebueno.arcade.api.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        String code,
        String message,
        Map<String, String> fields,
        LocalDateTime timestamp
) {

    public static ErrorResponse simple(String code, String message) {
        return new ErrorResponse(code, message, Map.of(), LocalDateTime.now());
    }

    public static ErrorResponse withFields(String code, String message, Map<String, String> fields) {
        return new ErrorResponse(code, message, fields, LocalDateTime.now());
    }
}
