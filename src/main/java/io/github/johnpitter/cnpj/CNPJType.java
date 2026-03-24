package io.github.johnpitter.cnpj;

/**
 * Tipo do CNPJ baseado no seu conteúdo.
 */
public enum CNPJType {

    /** CNPJ composto apenas por dígitos numéricos (formato legado). */
    NUMERIC,

    /** CNPJ contendo caracteres alfanuméricos (novo formato a partir de julho/2026). */
    ALPHANUMERIC
}
