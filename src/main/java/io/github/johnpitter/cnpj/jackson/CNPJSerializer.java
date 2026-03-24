package io.github.johnpitter.cnpj.jackson;

import io.github.johnpitter.cnpj.CNPJFormatter;

/**
 * Serializa um CNPJ aplicando formatação (XX.XXX.XXX/XXXX-XX).
 *
 * <p>Para uso com Jackson, registre via {@link CNPJModule} ou
 * use diretamente em um JsonSerializer customizado.</p>
 *
 * <p><b>Não requer Jackson no classpath.</b> Esta classe apenas
 * fornece a lógica de transformação.</p>
 */
public final class CNPJSerializer {

    private CNPJSerializer() {
    }

    /**
     * Serializa um CNPJ para formato com máscara.
     *
     * @param cnpj CNPJ sem formatação
     * @return CNPJ formatado (XX.XXX.XXX/XXXX-XX), ou null se input é null
     */
    public static String serialize(String cnpj) {
        if (cnpj == null || cnpj.isEmpty()) {
            return cnpj;
        }
        return CNPJFormatter.format(cnpj);
    }
}
