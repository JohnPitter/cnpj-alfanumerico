package io.github.johnpitter.cnpj;

import java.util.regex.Pattern;

/**
 * Validação de CNPJ alfanumérico e numérico.
 *
 * <p>Implementa o algoritmo Módulo 11 com conversão ASCII-48,
 * conforme Instrução Normativa RFB nº 2.229/2024.</p>
 *
 * <p>O algoritmo é retrocompatível: CNPJs numéricos existentes
 * produzem os mesmos dígitos verificadores.</p>
 */
public final class CNPJValidator {

    /** Regex para CNPJ sem formatação (12 alfanuméricos + 2 numéricos). */
    static final Pattern UNFORMATTED_PATTERN =
            Pattern.compile("^[A-Z0-9]{12}[0-9]{2}$");

    /** Regex para CNPJ com formatação (XX.XXX.XXX/XXXX-XX). */
    static final Pattern FORMATTED_PATTERN =
            Pattern.compile("^[A-Z0-9]{2}\\.[A-Z0-9]{3}\\.[A-Z0-9]{3}/[A-Z0-9]{4}-[0-9]{2}$");

    /** Regex para CNPJ legado (14 dígitos numéricos). */
    static final Pattern LEGACY_PATTERN = Pattern.compile("^[0-9]{14}$");

    /** Regex para CNPJ legado formatado. */
    static final Pattern LEGACY_FORMATTED_PATTERN =
            Pattern.compile("^[0-9]{2}\\.[0-9]{3}\\.[0-9]{3}/[0-9]{4}-[0-9]{2}$");

    /** Pesos para cálculo dos dígitos verificadores. */
    private static final int[] WEIGHTS = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    /** Valor base para conversão ASCII (código ASCII do caractere '0'). */
    private static final int ASCII_BASE = 48;

    private CNPJValidator() {
    }

    /**
     * Valida um CNPJ (com ou sem formatação).
     *
     * @param cnpj o CNPJ a ser validado
     * @return true se o CNPJ é válido
     */
    public static boolean isValid(String cnpj) {
        if (cnpj == null) {
            return false;
        }

        String cleaned = sanitize(cnpj);

        if (!UNFORMATTED_PATTERN.matcher(cleaned).matches()) {
            return false;
        }

        if (isAllSameChar(cleaned)) {
            return false;
        }

        String expectedCheckDigits = calculateCheckDigits(cleaned.substring(0, 12));
        return cleaned.substring(12).equals(expectedCheckDigits);
    }

    /**
     * Valida um CNPJ e lança exceção se for inválido.
     *
     * @param cnpj o CNPJ a ser validado
     * @throws CNPJException se o CNPJ é inválido
     */
    public static void validate(String cnpj) {
        if (cnpj == null) {
            throw new CNPJException("CNPJ não pode ser nulo");
        }

        String cleaned = sanitize(cnpj);

        if (!UNFORMATTED_PATTERN.matcher(cleaned).matches()) {
            throw new CNPJException("Formato inválido: CNPJ deve ter 12 caracteres alfanuméricos + 2 dígitos verificadores numéricos");
        }

        if (isAllSameChar(cleaned)) {
            throw new CNPJException("CNPJ inválido: todos os caracteres são iguais");
        }

        String expectedCheckDigits = calculateCheckDigits(cleaned.substring(0, 12));
        if (!cleaned.substring(12).equals(expectedCheckDigits)) {
            throw new CNPJException("Dígitos verificadores inválidos: esperado " + expectedCheckDigits
                    + ", recebido " + cleaned.substring(12));
        }
    }

    /**
     * Calcula os dois dígitos verificadores a partir dos 12 caracteres base.
     *
     * <p>Algoritmo:
     * <ol>
     *   <li>Converte cada caractere para valor numérico via ASCII - 48</li>
     *   <li>Multiplica pelos pesos [5,4,3,2,9,8,7,6,5,4,3,2] para DV1</li>
     *   <li>Soma os produtos e aplica Módulo 11</li>
     *   <li>Repete com pesos [6,5,4,3,2,9,8,7,6,5,4,3,2] incluindo DV1 para DV2</li>
     * </ol>
     *
     * @param base12 os 12 primeiros caracteres do CNPJ (alfanuméricos)
     * @return string com os 2 dígitos verificadores
     * @throws CNPJException se a base não tem 12 caracteres alfanuméricos válidos
     */
    public static String calculateCheckDigits(String base12) {
        if (base12 == null || base12.length() != 12) {
            throw new CNPJException("Base do CNPJ deve ter exatamente 12 caracteres");
        }

        String upper = base12.toUpperCase();

        if (!Pattern.matches("^[A-Z0-9]{12}$", upper)) {
            throw new CNPJException("Base do CNPJ deve conter apenas caracteres alfanuméricos (A-Z, 0-9)");
        }

        int sumDv1 = 0;
        int sumDv2 = 0;

        for (int i = 0; i < 12; i++) {
            int value = upper.charAt(i) - ASCII_BASE;
            sumDv1 += value * WEIGHTS[i + 1];
            sumDv2 += value * WEIGHTS[i];
        }

        int dv1 = sumDv1 % 11 < 2 ? 0 : 11 - (sumDv1 % 11);

        sumDv2 += dv1 * WEIGHTS[12];
        int dv2 = sumDv2 % 11 < 2 ? 0 : 11 - (sumDv2 % 11);

        return String.valueOf(dv1) + dv2;
    }

    /**
     * Verifica se o CNPJ contém apenas dígitos numéricos (formato legado).
     */
    static boolean isNumericOnly(String cleaned) {
        for (int i = 0; i < cleaned.length(); i++) {
            char c = cleaned.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica se o CNPJ contém caracteres alfabéticos (novo formato).
     */
    static boolean hasAlphaChars(String cleaned) {
        for (int i = 0; i < cleaned.length(); i++) {
            char c = cleaned.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                return true;
            }
        }
        return false;
    }

    /**
     * Remove formatação e converte para maiúsculas.
     */
    static String sanitize(String cnpj) {
        return cnpj.toUpperCase().replaceAll("[^A-Z0-9]", "");
    }

    private static boolean isAllSameChar(String value) {
        char first = value.charAt(0);
        for (int i = 1; i < value.length(); i++) {
            if (value.charAt(i) != first) {
                return false;
            }
        }
        return true;
    }
}
