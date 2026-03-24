package io.github.johnpitter.cnpj.jackson;

/**
 * Módulo de integração com Jackson para CNPJ.
 *
 * <p>Fornece métodos utilitários para serializar (formatar) e
 * deserializar (limpar) CNPJs em JSON.</p>
 *
 * <h3>Uso com Jackson (implementação manual):</h3>
 * <pre>{@code
 * // No seu JsonSerializer<String>:
 * gen.writeString(CNPJSerializer.serialize(value));
 *
 * // No seu JsonDeserializer<String>:
 * return CNPJDeserializer.deserialize(parser.getValueAsString());
 * }</pre>
 *
 * <h3>Uso com Spring Boot (via @JsonSerialize/@JsonDeserialize):</h3>
 * <pre>{@code
 * public class EmpresaDTO {
 *     // Customize com seus próprios serializers que delegam para CNPJSerializer/CNPJDeserializer
 *     private String cnpj;
 * }
 * }</pre>
 *
 * <p><b>Nota:</b> Esta classe não depende do Jackson diretamente.
 * Use {@link CNPJSerializer} e {@link CNPJDeserializer} para a lógica
 * de transformação e integre com seu framework preferido.</p>
 *
 * @see CNPJSerializer
 * @see CNPJDeserializer
 */
public final class CNPJModule {

    /** Nome do módulo para registro em ObjectMapper. */
    public static final String MODULE_NAME = "cnpj-alfanumerico";

    /** Versão do módulo. */
    public static final String MODULE_VERSION = "1.1.0";

    private CNPJModule() {
    }
}
