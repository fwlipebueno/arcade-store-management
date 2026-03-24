package br.edu.felipebueno.arcade.util;

import java.math.BigDecimal;
import java.util.Scanner;

public final class InputUtils {
    private InputUtils() {
    }

    public static String lerTexto(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim();

            if (!valor.isBlank()) {
                return valor;
            }

            System.out.println("Valor inválido. Tente novamente.");
        }
    }

    public static int lerInteiro(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim();

            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException exception) {
                System.out.println("Número inteiro inválido. Tente novamente.");
            }
        }
    }

    public static long lerLong(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim();

            try {
                return Long.parseLong(valor);
            } catch (NumberFormatException exception) {
                System.out.println("Número inválido. Tente novamente.");
            }
        }
    }

    public static BigDecimal lerBigDecimal(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim().replace(',', '.');

            try {
                return new BigDecimal(valor);
            } catch (NumberFormatException exception) {
                System.out.println("Valor monetário inválido. Tente novamente.");
            }
        }
    }
}
