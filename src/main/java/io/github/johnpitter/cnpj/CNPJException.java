package io.github.johnpitter.cnpj;

/**
 * Exceção lançada quando um CNPJ é inválido ou uma operação sobre CNPJ falha.
 */
public class CNPJException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public CNPJException(String message) {
        super(message);
    }

    public CNPJException(String message, Throwable cause) {
        super(message, cause);
    }
}
