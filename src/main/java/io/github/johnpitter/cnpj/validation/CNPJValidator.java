package io.github.johnpitter.cnpj.validation;

/**
 * Validador para a annotation {@link CNPJ}.
 *
 * <p>Este validador é independente de framework — não importa
 * jakarta.validation nem javax.validation diretamente.</p>
 *
 * <p>Para integração com Bean Validation, registre este validador
 * no seu framework de validação. Exemplo com Spring Boot:</p>
 *
 * <pre>{@code
 * // A annotation @CNPJ pode ser usada com um ConstraintValidator customizado
 * // que delega para CNPJValidator.isValid()
 * }</pre>
 *
 * @see io.github.johnpitter.cnpj.CNPJ#isValid(String)
 */
public final class CNPJValidator {

    private CNPJValidator() {
    }

    /**
     * Valida um CNPJ para uso em annotations de validação.
     *
     * <p>Retorna true para valores nulos (use @NotNull separadamente).</p>
     *
     * @param value o valor a ser validado
     * @return true se nulo ou CNPJ válido
     */
    public static boolean isValid(Object value) {
        if (value == null) {
            return true;
        }
        if (!(value instanceof String)) {
            return false;
        }
        return io.github.johnpitter.cnpj.CNPJValidator.isValid((String) value);
    }
}
