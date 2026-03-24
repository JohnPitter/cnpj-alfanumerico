package io.github.johnpitter.cnpj.jackson;

import io.github.johnpitter.cnpj.CNPJFormatter;

/**
 * Deserializa um CNPJ removendo formatação e normalizando para uppercase.
 *
 * <p>Para uso com Jackson, registre via {@link CNPJModule} ou
 * use diretamente em um JsonDeserializer customizado.</p>
 *
 * <p><b>Não requer Jackson no classpath.</b> Esta classe apenas
 * fornece a lógica de transformação.</p>
 */
public final class CNPJDeserializer {

    private CNPJDeserializer() {
    }

    /**
     * Deserializa um CNPJ removendo formatação.
     *
     * @param cnpj CNPJ com ou sem formatação
     * @return CNPJ sem formatação (14 chars, uppercase), ou null se input é null
     */
    public static String deserialize(String cnpj) {
        if (cnpj == null || cnpj.isEmpty()) {
            return cnpj;
        }
        return CNPJFormatter.unformat(cnpj);
    }
}
