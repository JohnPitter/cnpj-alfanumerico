package io.github.joaop.cnpj;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Gerador de CNPJs válidos para uso em testes.
 *
 * <p><b>ATENÇÃO:</b> CNPJs gerados são matematicamente válidos
 * mas NÃO representam empresas reais. Use apenas para testes.</p>
 */
public final class CNPJGenerator {

    private static final String ALPHANUMERIC_CHARS = "0123456789ABCDEFGHJKLMNPRSTVWXYZ";
    private static final String NUMERIC_CHARS = "0123456789";
    private static final Random RANDOM = new SecureRandom();

    private CNPJGenerator() {
    }

    /**
     * Gera um CNPJ alfanumérico válido aleatório.
     *
     * <p>Garante que pelo menos um caractere alfabético está presente
     * nos 12 primeiros caracteres.</p>
     *
     * @return CNPJ alfanumérico válido sem formatação
     */
    public static String generate() {
        String base;
        do {
            base = randomString(ALPHANUMERIC_CHARS, 12);
        } while (CNPJValidator.isNumericOnly(base));

        return base + CNPJValidator.calculateCheckDigits(base);
    }

    /**
     * Gera um CNPJ alfanumérico válido e retorna formatado.
     *
     * @return CNPJ alfanumérico válido com formatação (XX.XXX.XXX/XXXX-XX)
     */
    public static String generateFormatted() {
        return CNPJFormatter.format(generate());
    }

    /**
     * Gera um CNPJ numérico válido aleatório (formato legado).
     *
     * @return CNPJ numérico válido sem formatação
     */
    public static String generateNumeric() {
        String base = randomString(NUMERIC_CHARS, 12);
        return base + CNPJValidator.calculateCheckDigits(base);
    }

    /**
     * Gera um CNPJ numérico válido e retorna formatado.
     *
     * @return CNPJ numérico válido com formatação
     */
    public static String generateNumericFormatted() {
        return CNPJFormatter.format(generateNumeric());
    }

    /**
     * Gera um CNPJ válido a partir de uma raiz (8 caracteres) e ordem (4 caracteres).
     *
     * @param root raiz do CNPJ (8 caracteres alfanuméricos)
     * @param branch ordem do estabelecimento (4 caracteres alfanuméricos)
     * @return CNPJ completo com dígitos verificadores calculados
     * @throws CNPJException se root ou branch têm tamanho inválido
     */
    public static String generateFromParts(String root, String branch) {
        if (root == null || root.length() != 8) {
            throw new CNPJException("Raiz do CNPJ deve ter exatamente 8 caracteres");
        }
        if (branch == null || branch.length() != 4) {
            throw new CNPJException("Ordem do CNPJ deve ter exatamente 4 caracteres");
        }

        String base = root.toUpperCase() + branch.toUpperCase();
        return base + CNPJValidator.calculateCheckDigits(base);
    }

    /**
     * Gera um CNPJ válido a partir dos 12 caracteres base.
     *
     * @param base12 os 12 primeiros caracteres do CNPJ
     * @return CNPJ completo com dígitos verificadores calculados
     */
    public static String generateFromBase(String base12) {
        if (base12 == null || base12.length() != 12) {
            throw new CNPJException("Base do CNPJ deve ter exatamente 12 caracteres");
        }

        String upper = base12.toUpperCase();
        return upper + CNPJValidator.calculateCheckDigits(upper);
    }

    /**
     * Gera um CNPJ de matriz válido a partir da raiz.
     *
     * @param root raiz do CNPJ (8 caracteres alfanuméricos)
     * @return CNPJ de matriz (branch = 0001) com dígitos verificadores
     */
    public static String generateHeadquarters(String root) {
        return generateFromParts(root, "0001");
    }

    private static String randomString(String chars, int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
