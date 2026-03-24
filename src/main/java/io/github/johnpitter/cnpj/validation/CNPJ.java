package io.github.johnpitter.cnpj.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Valida que o campo anotado contém um CNPJ válido (numérico ou alfanumérico).
 *
 * <p>Requer Jakarta Validation API (jakarta.validation) ou Bean Validation API
 * (javax.validation) no classpath.</p>
 *
 * <h3>Uso:</h3>
 * <pre>{@code
 * public class EmpresaDTO {
 *     @CNPJ
 *     private String cnpj;
 *
 *     @CNPJ(message = "CNPJ da filial inválido")
 *     private String cnpjFilial;
 * }
 * }</pre>
 *
 * <p>Campos nulos são considerados válidos (use {@code @NotNull} se necessário).</p>
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CNPJ {

    String message() default "CNPJ inválido";

    Class<?>[] groups() default {};

    @SuppressWarnings("rawtypes")
    Class[] payload() default {};
}
