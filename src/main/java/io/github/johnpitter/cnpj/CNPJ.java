package io.github.johnpitter.cnpj;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Classe principal da biblioteca CNPJ Alfanumérico.
 *
 * <p>Fachada com métodos estáticos para validação, formatação, geração
 * e análise de CNPJs nos formatos numérico (legado) e alfanumérico
 * (Instrução Normativa RFB nº 2.229/2024).</p>
 *
 * <h3>Uso rápido:</h3>
 * <pre>{@code
 * // Validação
 * CNPJ.isValid("12.ABC.345/01DE-35");   // true
 * CNPJ.isValid("11.222.333/0001-81");   // true (legado)
 *
 * // Formatação
 * CNPJ.format("12ABC34501DE35");         // "12.ABC.345/01DE-35"
 * CNPJ.unformat("12.ABC.345/01DE-35");   // "12ABC34501DE35"
 *
 * // Geração (para testes)
 * CNPJ.generate();                       // CNPJ alfanumérico aleatório
 * CNPJ.generateNumeric();                // CNPJ numérico aleatório
 *
 * // Análise
 * CNPJ.parse("12ABC34501DE35").getRoot();       // "12ABC345"
 * CNPJ.parse("12ABC34501DE35").isHeadquarters(); // false
 * }</pre>
 *
 * @author João Pitter
 * @see <a href="https://www.gov.br/receitafederal/pt-br/centrais-de-conteudo/publicacoes/documentos-tecnicos/cnpj">
 *      Documentação Técnica - Receita Federal</a>
 */
public final class CNPJ {

    private CNPJ() {
    }

    // ========================================================================
    // Validação
    // ========================================================================

    /**
     * Valida um CNPJ (com ou sem formatação, numérico ou alfanumérico).
     *
     * @param cnpj o CNPJ a ser validado
     * @return true se o CNPJ é válido
     */
    public static boolean isValid(String cnpj) {
        return CNPJValidator.isValid(cnpj);
    }

    /**
     * Valida um CNPJ e lança {@link CNPJException} se for inválido.
     *
     * @param cnpj o CNPJ a ser validado
     * @throws CNPJException se o CNPJ é inválido
     */
    public static void validate(String cnpj) {
        CNPJValidator.validate(cnpj);
    }

    // ========================================================================
    // Dígitos verificadores
    // ========================================================================

    /**
     * Calcula os dois dígitos verificadores a partir dos 12 caracteres base.
     *
     * @param base12 os 12 primeiros caracteres do CNPJ
     * @return string com os 2 dígitos verificadores
     * @throws CNPJException se a base é inválida
     */
    public static String calculateCheckDigits(String base12) {
        return CNPJValidator.calculateCheckDigits(base12);
    }

    // ========================================================================
    // Formatação
    // ========================================================================

    /**
     * Formata um CNPJ no padrão XX.XXX.XXX/XXXX-XX.
     *
     * @param cnpj CNPJ sem formatação
     * @return CNPJ formatado
     * @throws CNPJException se o CNPJ é inválido
     */
    public static String format(String cnpj) {
        return CNPJFormatter.format(cnpj);
    }

    /**
     * Remove a formatação de um CNPJ.
     *
     * @param cnpj CNPJ com ou sem formatação
     * @return CNPJ sem formatação (14 caracteres, uppercase)
     * @throws CNPJException se o CNPJ é inválido
     */
    public static String unformat(String cnpj) {
        return CNPJFormatter.unformat(cnpj);
    }

    /**
     * Verifica se o CNPJ está no formato com máscara.
     *
     * @param cnpj o CNPJ a ser verificado
     * @return true se está formatado (XX.XXX.XXX/XXXX-XX)
     */
    public static boolean isFormatted(String cnpj) {
        return CNPJFormatter.isFormatted(cnpj);
    }

    /**
     * Verifica se o CNPJ está no formato sem máscara.
     *
     * @param cnpj o CNPJ a ser verificado
     * @return true se está sem formatação
     */
    public static boolean isUnformatted(String cnpj) {
        return CNPJFormatter.isUnformatted(cnpj);
    }

    // ========================================================================
    // Classificação e detecção
    // ========================================================================

    /**
     * Verifica se o CNPJ contém caracteres alfabéticos (novo formato alfanumérico).
     *
     * @param cnpj o CNPJ a ser verificado (com ou sem formatação)
     * @return true se contém letras
     */
    public static boolean isAlphanumeric(String cnpj) {
        if (cnpj == null) return false;
        return CNPJValidator.hasAlphaChars(CNPJValidator.sanitize(cnpj));
    }

    /**
     * Verifica se o CNPJ contém apenas dígitos numéricos (formato legado).
     *
     * @param cnpj o CNPJ a ser verificado (com ou sem formatação)
     * @return true se contém apenas números
     */
    public static boolean isNumeric(String cnpj) {
        if (cnpj == null) return false;
        return CNPJValidator.isNumericOnly(CNPJValidator.sanitize(cnpj));
    }

    /**
     * Retorna o tipo do CNPJ: {@link CNPJType#NUMERIC} ou {@link CNPJType#ALPHANUMERIC}.
     *
     * @param cnpj o CNPJ a ser analisado (com ou sem formatação)
     * @return o tipo do CNPJ
     * @throws CNPJException se o CNPJ é nulo
     */
    public static CNPJType getType(String cnpj) {
        if (cnpj == null) {
            throw new CNPJException("CNPJ não pode ser nulo");
        }
        String cleaned = CNPJValidator.sanitize(cnpj);
        return CNPJValidator.isNumericOnly(cleaned) ? CNPJType.NUMERIC : CNPJType.ALPHANUMERIC;
    }

    // ========================================================================
    // Extração de partes
    // ========================================================================

    /**
     * Decompõe o CNPJ em suas partes (raiz, ordem, dígitos verificadores).
     *
     * @param cnpj CNPJ com ou sem formatação
     * @return objeto {@link CNPJParts} com as partes decompostas
     * @throws CNPJException se o CNPJ não tem 14 caracteres válidos
     */
    public static CNPJParts parse(String cnpj) {
        String cleaned = CNPJFormatter.unformat(cnpj);
        return new CNPJParts(
                cleaned.substring(0, 8),
                cleaned.substring(8, 12),
                cleaned.substring(12, 14)
        );
    }

    /**
     * Extrai a raiz do CNPJ (8 primeiros caracteres — identificador da empresa).
     *
     * @param cnpj CNPJ com ou sem formatação
     * @return raiz do CNPJ (8 caracteres)
     */
    public static String getRoot(String cnpj) {
        return parse(cnpj).getRoot();
    }

    /**
     * Extrai a ordem do estabelecimento (posições 9-12).
     *
     * @param cnpj CNPJ com ou sem formatação
     * @return ordem do estabelecimento (4 caracteres)
     */
    public static String getBranch(String cnpj) {
        return parse(cnpj).getBranch();
    }

    /**
     * Extrai os dígitos verificadores (posições 13-14).
     *
     * @param cnpj CNPJ com ou sem formatação
     * @return dígitos verificadores (2 caracteres numéricos)
     */
    public static String getCheckDigits(String cnpj) {
        return parse(cnpj).getCheckDigits();
    }

    /**
     * Verifica se o CNPJ é de uma matriz (ordem = 0001).
     *
     * @param cnpj CNPJ com ou sem formatação
     * @return true se é matriz
     */
    public static boolean isHeadquarters(String cnpj) {
        return parse(cnpj).isHeadquarters();
    }

    // ========================================================================
    // Geração (para testes)
    // ========================================================================

    /**
     * Gera um CNPJ alfanumérico válido aleatório.
     *
     * <p><b>Uso exclusivo para testes.</b> CNPJs gerados não representam empresas reais.</p>
     *
     * @return CNPJ alfanumérico válido sem formatação
     */
    public static String generate() {
        return CNPJGenerator.generate();
    }

    /**
     * Gera um CNPJ alfanumérico válido e retorna formatado.
     *
     * @return CNPJ alfanumérico formatado (XX.XXX.XXX/XXXX-XX)
     */
    public static String generateFormatted() {
        return CNPJGenerator.generateFormatted();
    }

    /**
     * Gera um CNPJ numérico válido aleatório (formato legado).
     *
     * @return CNPJ numérico válido sem formatação
     */
    public static String generateNumeric() {
        return CNPJGenerator.generateNumeric();
    }

    /**
     * Gera um CNPJ numérico válido e retorna formatado.
     *
     * @return CNPJ numérico formatado
     */
    public static String generateNumericFormatted() {
        return CNPJGenerator.generateNumericFormatted();
    }

    /**
     * Gera um CNPJ válido a partir de uma raiz e ordem.
     *
     * @param root raiz do CNPJ (8 caracteres)
     * @param branch ordem do estabelecimento (4 caracteres)
     * @return CNPJ completo com dígitos verificadores
     */
    public static String generateFromParts(String root, String branch) {
        return CNPJGenerator.generateFromParts(root, branch);
    }

    /**
     * Gera um CNPJ válido a partir dos 12 caracteres base.
     *
     * @param base12 os 12 primeiros caracteres
     * @return CNPJ completo com dígitos verificadores
     */
    public static String generateFromBase(String base12) {
        return CNPJGenerator.generateFromBase(base12);
    }

    /**
     * Gera um CNPJ de matriz válido a partir da raiz (branch = 0001).
     *
     * @param root raiz do CNPJ (8 caracteres)
     * @return CNPJ de matriz completo
     */
    public static String generateHeadquarters(String root) {
        return CNPJGenerator.generateHeadquarters(root);
    }

    // ========================================================================
    // Regex patterns
    // ========================================================================

    /**
     * Retorna o {@link Pattern} regex para CNPJ sem formatação.
     *
     * <p>Pattern: {@code [A-Z0-9]{12}[0-9]{2}}</p>
     *
     * @return Pattern compilado
     */
    public static Pattern getPattern() {
        return CNPJValidator.UNFORMATTED_PATTERN;
    }

    /**
     * Retorna o {@link Pattern} regex para CNPJ com formatação.
     *
     * <p>Pattern: {@code [A-Z0-9]{2}\.[A-Z0-9]{3}\.[A-Z0-9]{3}/[A-Z0-9]{4}-[0-9]{2}}</p>
     *
     * @return Pattern compilado
     */
    public static Pattern getFormattedPattern() {
        return CNPJValidator.FORMATTED_PATTERN;
    }

    /**
     * Retorna o {@link Pattern} regex para CNPJ legado (numérico).
     *
     * <p>Pattern: {@code [0-9]{14}}</p>
     *
     * @return Pattern compilado
     */
    public static Pattern getLegacyPattern() {
        return CNPJValidator.LEGACY_PATTERN;
    }

    // ========================================================================
    // Comparação e igualdade
    // ========================================================================

    /**
     * Compara dois CNPJs ignorando formatação e case.
     *
     * @param cnpj1 primeiro CNPJ
     * @param cnpj2 segundo CNPJ
     * @return true se representam o mesmo CNPJ
     */
    public static boolean equals(String cnpj1, String cnpj2) {
        if (cnpj1 == null || cnpj2 == null) return false;
        return CNPJValidator.sanitize(cnpj1).equals(CNPJValidator.sanitize(cnpj2));
    }

    /**
     * Verifica se dois CNPJs pertencem à mesma empresa (mesma raiz).
     *
     * @param cnpj1 primeiro CNPJ
     * @param cnpj2 segundo CNPJ
     * @return true se têm a mesma raiz (primeiros 8 caracteres)
     */
    public static boolean isSameCompany(String cnpj1, String cnpj2) {
        if (cnpj1 == null || cnpj2 == null) return false;
        return getRoot(cnpj1).equals(getRoot(cnpj2));
    }

    // ========================================================================
    // Migração e compatibilidade de storage
    // ========================================================================

    /**
     * Verifica se o CNPJ requer migração de colunas numéricas para VARCHAR.
     *
     * <p>Retorna true se o CNPJ contém letras e portanto não pode
     * ser armazenado em colunas BIGINT/NUMERIC.</p>
     *
     * @param cnpj o CNPJ a ser verificado
     * @return true se requer migração de storage
     */
    public static boolean requiresStorageMigration(String cnpj) {
        return isAlphanumeric(cnpj);
    }

    /**
     * Retorna o tipo mínimo de coluna SQL para armazenar o CNPJ.
     *
     * @param cnpj o CNPJ a ser verificado
     * @return "BIGINT" para numéricos, "CHAR(14)" para alfanuméricos
     */
    public static String getMinColumnType(String cnpj) {
        return isAlphanumeric(cnpj) ? "CHAR(14)" : "BIGINT";
    }

    // ========================================================================
    // Máscara LGPD
    // ========================================================================

    /**
     * Mascara um CNPJ para exibição segura (LGPD).
     *
     * <p>Formato padrão: {@code 12.***.345/****-**}</p>
     *
     * @param cnpj o CNPJ a ser mascarado (com ou sem formatação)
     * @return CNPJ mascarado no formato XX.***.XXX/****-**
     * @throws CNPJException se o CNPJ é inválido
     */
    public static String mask(String cnpj) {
        String cleaned = CNPJFormatter.unformat(cnpj);
        return cleaned.substring(0, 2) + ".***.***/" + cleaned.substring(8, 12) + "-**";
    }

    /**
     * Mascara um CNPJ exibindo apenas os N primeiros caracteres.
     *
     * @param cnpj o CNPJ a ser mascarado (com ou sem formatação)
     * @param visibleChars quantidade de caracteres visíveis no início (1-12)
     * @return CNPJ parcialmente mascarado
     * @throws CNPJException se o CNPJ é inválido ou visibleChars fora do range
     */
    public static String mask(String cnpj, int visibleChars) {
        if (visibleChars < 1 || visibleChars > 12) {
            throw new CNPJException("visibleChars deve estar entre 1 e 12, recebido: " + visibleChars);
        }
        String cleaned = CNPJFormatter.unformat(cnpj);
        StringBuilder sb = new StringBuilder(14);
        sb.append(cleaned, 0, visibleChars);
        for (int i = visibleChars; i < 14; i++) {
            sb.append('*');
        }
        return sb.toString();
    }

    // ========================================================================
    // Validação em lote
    // ========================================================================

    /**
     * Valida uma coleção de CNPJs e retorna resultado detalhado.
     *
     * @param cnpjs coleção de CNPJs a serem validados
     * @return {@link CNPJBatchResult} com válidos, inválidos e contagens
     */
    public static CNPJBatchResult validateBatch(Collection<String> cnpjs) {
        if (cnpjs == null) {
            throw new CNPJException("Coleção de CNPJs não pode ser nula");
        }
        CNPJBatchResult result = new CNPJBatchResult();
        for (String cnpj : cnpjs) {
            try {
                CNPJValidator.validate(cnpj);
                result.addValid(CNPJValidator.sanitize(cnpj));
            } catch (CNPJException e) {
                result.addInvalid(cnpj, e.getMessage());
            }
        }
        return result;
    }

    // ========================================================================
    // Código de barras
    // ========================================================================

    /**
     * Retorna o encoding de código de barras adequado para o CNPJ.
     *
     * <p>CNPJs numéricos usam CODE-128C (mais compacto).
     * CNPJs alfanuméricos precisam de CODE-128A.</p>
     *
     * @param cnpj o CNPJ a ser verificado
     * @return {@link BarcodeEncoding#CODE_128C} ou {@link BarcodeEncoding#CODE_128A}
     */
    public static BarcodeEncoding getBarcodeEncoding(String cnpj) {
        if (cnpj == null) {
            throw new CNPJException("CNPJ não pode ser nulo");
        }
        return isAlphanumeric(cnpj) ? BarcodeEncoding.CODE_128A : BarcodeEncoding.CODE_128C;
    }

    // ========================================================================
    // Listagem de filiais
    // ========================================================================

    /**
     * Gera CNPJs completos (com DVs) para uma lista de filiais a partir da raiz.
     *
     * @param root raiz do CNPJ (8 caracteres)
     * @param branches lista de ordens de estabelecimento (4 caracteres cada)
     * @return lista de CNPJs completos com dígitos verificadores calculados
     */
    public static List<String> listBranches(String root, Collection<String> branches) {
        if (root == null || root.length() != 8) {
            throw new CNPJException("Raiz do CNPJ deve ter exatamente 8 caracteres");
        }
        if (branches == null) {
            throw new CNPJException("Lista de filiais não pode ser nula");
        }
        List<String> result = new ArrayList<String>(branches.size());
        for (String branch : branches) {
            result.add(CNPJGenerator.generateFromParts(root, branch));
        }
        return result;
    }

    // ========================================================================
    // Regex patterns
    // ========================================================================

    /**
     * Retorna o {@link Pattern} regex para CNPJ legado formatado.
     *
     * <p>Pattern: {@code [0-9]{2}\.[0-9]{3}\.[0-9]{3}/[0-9]{4}-[0-9]{2}}</p>
     *
     * @return Pattern compilado
     */
    public static Pattern getLegacyFormattedPattern() {
        return CNPJValidator.LEGACY_FORMATTED_PATTERN;
    }
}
