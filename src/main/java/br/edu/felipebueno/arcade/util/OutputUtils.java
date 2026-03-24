package br.edu.felipebueno.arcade.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class OutputUtils {
    private static final Locale LOCALE_BR = Locale.forLanguageTag("pt-BR");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private OutputUtils() {
    }

    public static String formatarMoeda(BigDecimal valor) {
        return NumberFormat.getCurrencyInstance(LOCALE_BR).format(valor);
    }

    public static String formatarDataHora(LocalDateTime dataHora) {
        return dataHora.format(DATE_TIME_FORMATTER);
    }

    public static String valorOuPadrao(String valor) {
        return valor == null || valor.isBlank() ? "-" : valor;
    }
}
