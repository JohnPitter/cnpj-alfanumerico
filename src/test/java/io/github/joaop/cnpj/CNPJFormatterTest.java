package io.github.joaop.cnpj;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CNPJFormatter")
class CNPJFormatterTest {

    @Nested
    @DisplayName("format")
    class Format {

        @Test
        @DisplayName("deve formatar CNPJ alfanumérico")
        void shouldFormatAlphanumeric() {
            assertEquals("12.ABC.345/01DE-35", CNPJFormatter.format("12ABC34501DE35"));
        }

        @Test
        @DisplayName("deve formatar CNPJ numérico")
        void shouldFormatNumeric() {
            assertEquals("11.222.333/0001-81", CNPJFormatter.format("11222333000181"));
        }

        @Test
        @DisplayName("deve formatar CNPJ já formatado (idempotente)")
        void shouldHandleAlreadyFormatted() {
            assertEquals("12.ABC.345/01DE-35", CNPJFormatter.format("12.ABC.345/01DE-35"));
        }

        @Test
        @DisplayName("deve converter para maiúsculas")
        void shouldConvertToUppercase() {
            assertEquals("12.ABC.345/01DE-35", CNPJFormatter.format("12abc34501de35"));
        }

        @Test
        @DisplayName("deve lançar exceção para nulo")
        void shouldThrowForNull() {
            assertThrows(CNPJException.class, () -> CNPJFormatter.format(null));
        }

        @Test
        @DisplayName("deve lançar exceção para tamanho inválido")
        void shouldThrowForInvalidLength() {
            assertThrows(CNPJException.class, () -> CNPJFormatter.format("123"));
        }
    }

    @Nested
    @DisplayName("unformat")
    class Unformat {

        @Test
        @DisplayName("deve remover formatação alfanumérica")
        void shouldUnformatAlphanumeric() {
            assertEquals("12ABC34501DE35", CNPJFormatter.unformat("12.ABC.345/01DE-35"));
        }

        @Test
        @DisplayName("deve remover formatação numérica")
        void shouldUnformatNumeric() {
            assertEquals("11222333000181", CNPJFormatter.unformat("11.222.333/0001-81"));
        }

        @Test
        @DisplayName("deve retornar mesmo valor se já sem formatação")
        void shouldHandleAlreadyUnformatted() {
            assertEquals("12ABC34501DE35", CNPJFormatter.unformat("12ABC34501DE35"));
        }

        @Test
        @DisplayName("deve converter para maiúsculas")
        void shouldConvertToUppercase() {
            assertEquals("12ABC34501DE35", CNPJFormatter.unformat("12abc34501de35"));
        }
    }

    @Nested
    @DisplayName("isFormatted")
    class IsFormatted {

        @Test
        @DisplayName("deve retornar true para CNPJ formatado alfanumérico")
        void shouldReturnTrueForFormattedAlpha() {
            assertTrue(CNPJFormatter.isFormatted("12.ABC.345/01DE-35"));
        }

        @Test
        @DisplayName("deve retornar true para CNPJ formatado numérico")
        void shouldReturnTrueForFormattedNumeric() {
            assertTrue(CNPJFormatter.isFormatted("11.222.333/0001-81"));
        }

        @Test
        @DisplayName("deve retornar false para CNPJ sem formatação")
        void shouldReturnFalseForUnformatted() {
            assertFalse(CNPJFormatter.isFormatted("12ABC34501DE35"));
        }

        @Test
        @DisplayName("deve retornar false para nulo")
        void shouldReturnFalseForNull() {
            assertFalse(CNPJFormatter.isFormatted(null));
        }
    }

    @Nested
    @DisplayName("isUnformatted")
    class IsUnformatted {

        @Test
        @DisplayName("deve retornar true para CNPJ sem formatação")
        void shouldReturnTrueForUnformatted() {
            assertTrue(CNPJFormatter.isUnformatted("12ABC34501DE35"));
        }

        @Test
        @DisplayName("deve retornar false para CNPJ formatado")
        void shouldReturnFalseForFormatted() {
            assertFalse(CNPJFormatter.isUnformatted("12.ABC.345/01DE-35"));
        }
    }
}
