package io.github.johnpitter.cnpj;

import java.util.regex.Pattern;

/**
 * Formatação e desformatação de CNPJ.
 *
 * <p>Formato padrão: {@code XX.XXX.XXX/XXXX-XX}</p>
 */
public final class CNPJFormatter {

    private static final Pattern NON_ALNUM = Pattern.compile("[^A-Z0-9]");

    private CNPJFormatter() {
    }

    /**
     * Formata um CNPJ no padrão XX.XXX.XXX/XXXX-XX.
     *
     * @param cnpj CNPJ com ou sem formatação (14 caracteres alfanuméricos)
     * @return CNPJ formatado
     * @throws CNPJException se o CNPJ não tem 14 caracteres válidos
     */
    public static String format(String cnpj) {
        if (cnpj == null) {
            throw new CNPJException("CNPJ não pode ser nulo");
        }

        String cleaned = NON_ALNUM.matcher(cnpj.toUpperCase()).replaceAll("");

        if (cleaned.length() != 14) {
            throw new CNPJException("CNPJ deve ter 14 caracteres, recebido: " + cleaned.length());
        }

        return cleaned.substring(0, 2) + "."
                + cleaned.substring(2, 5) + "."
                + cleaned.substring(5, 8) + "/"
                + cleaned.substring(8, 12) + "-"
                + cleaned.substring(12, 14);
    }

    /**
     * Remove a formatação de um CNPJ (pontos, barra, hífen).
     *
     * @param cnpj CNPJ com ou sem formatação
     * @return CNPJ sem formatação (14 caracteres), em maiúsculas
     * @throws CNPJException se o resultado não tem 14 caracteres válidos
     */
    public static String unformat(String cnpj) {
        if (cnpj == null) {
            throw new CNPJException("CNPJ não pode ser nulo");
        }

        String cleaned = NON_ALNUM.matcher(cnpj.toUpperCase()).replaceAll("");

        if (cleaned.length() != 14) {
            throw new CNPJException("CNPJ deve ter 14 caracteres, recebido: " + cleaned.length());
        }

        return cleaned;
    }

    /**
     * Verifica se o CNPJ está no formato com máscara (XX.XXX.XXX/XXXX-XX).
     *
     * @param cnpj o CNPJ a ser verificado
     * @return true se está formatado
     */
    public static boolean isFormatted(String cnpj) {
        if (cnpj == null) {
            return false;
        }
        return CNPJValidator.FORMATTED_PATTERN.matcher(cnpj.toUpperCase()).matches();
    }

    /**
     * Verifica se o CNPJ está no formato sem máscara (14 caracteres consecutivos).
     *
     * @param cnpj o CNPJ a ser verificado
     * @return true se está sem formatação
     */
    public static boolean isUnformatted(String cnpj) {
        if (cnpj == null) {
            return false;
        }
        return CNPJValidator.UNFORMATTED_PATTERN.matcher(cnpj.toUpperCase()).matches();
    }
}
