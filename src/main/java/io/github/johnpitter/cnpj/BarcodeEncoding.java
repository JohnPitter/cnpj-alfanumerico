package io.github.johnpitter.cnpj;

/**
 * Tipo de encoding de código de barras adequado para o CNPJ.
 *
 * <p>CNPJs numéricos podem usar CODE-128C (mais compacto, somente dígitos).
 * CNPJs alfanuméricos precisam de CODE-128A (suporta letras).</p>
 */
public enum BarcodeEncoding {

    /** CODE-128C — otimizado para dígitos numéricos. Usado por CNPJs legados. */
    CODE_128C,

    /** CODE-128A — suporta caracteres alfanuméricos. Necessário para novos CNPJs. */
    CODE_128A
}
