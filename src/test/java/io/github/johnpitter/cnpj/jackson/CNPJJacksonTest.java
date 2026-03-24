package io.github.johnpitter.cnpj.jackson;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Jackson — Serializer/Deserializer")
class CNPJJacksonTest {

    @Nested
    @DisplayName("CNPJSerializer")
    class Serialize {

        @Test
        @DisplayName("deve formatar CNPJ na serialização")
        void shouldFormatOnSerialize() {
            assertEquals("12.ABC.345/01DE-35", CNPJSerializer.serialize("12ABC34501DE35"));
        }

        @Test
        @DisplayName("deve formatar CNPJ numérico")
        void shouldFormatNumeric() {
            assertEquals("11.222.333/0001-81", CNPJSerializer.serialize("11222333000181"));
        }

        @Test
        @DisplayName("deve retornar null para null")
        void shouldReturnNullForNull() {
            assertNull(CNPJSerializer.serialize(null));
        }

        @Test
        @DisplayName("deve retornar vazio para vazio")
        void shouldReturnEmptyForEmpty() {
            assertEquals("", CNPJSerializer.serialize(""));
        }
    }

    @Nested
    @DisplayName("CNPJDeserializer")
    class Deserialize {

        @Test
        @DisplayName("deve remover formatação na deserialização")
        void shouldUnformatOnDeserialize() {
            assertEquals("12ABC34501DE35", CNPJDeserializer.deserialize("12.ABC.345/01DE-35"));
        }

        @Test
        @DisplayName("deve normalizar para uppercase")
        void shouldNormalizeToUppercase() {
            assertEquals("12ABC34501DE35", CNPJDeserializer.deserialize("12abc34501de35"));
        }

        @Test
        @DisplayName("deve retornar null para null")
        void shouldReturnNullForNull() {
            assertNull(CNPJDeserializer.deserialize(null));
        }

        @Test
        @DisplayName("deve retornar vazio para vazio")
        void shouldReturnEmptyForEmpty() {
            assertEquals("", CNPJDeserializer.deserialize(""));
        }
    }

    @Nested
    @DisplayName("CNPJModule")
    class Module {

        @Test
        @DisplayName("deve ter nome e versão definidos")
        void shouldHaveNameAndVersion() {
            assertEquals("cnpj-alfanumerico", CNPJModule.MODULE_NAME);
            assertNotNull(CNPJModule.MODULE_VERSION);
        }
    }
}
