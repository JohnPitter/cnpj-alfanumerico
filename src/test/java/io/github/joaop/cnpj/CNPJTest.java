package io.github.joaop.cnpj;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CNPJ (facade)")
class CNPJTest {

    @Nested
    @DisplayName("classificação")
    class Classification {

        @Test
        @DisplayName("deve detectar CNPJ alfanumérico")
        void shouldDetectAlphanumeric() {
            assertTrue(CNPJ.isAlphanumeric("12ABC34501DE35"));
            assertFalse(CNPJ.isAlphanumeric("11222333000181"));
        }

        @Test
        @DisplayName("deve detectar CNPJ numérico")
        void shouldDetectNumeric() {
            assertTrue(CNPJ.isNumeric("11222333000181"));
            assertFalse(CNPJ.isNumeric("12ABC34501DE35"));
        }

        @Test
        @DisplayName("deve retornar tipo correto")
        void shouldReturnCorrectType() {
            assertEquals(CNPJType.ALPHANUMERIC, CNPJ.getType("12ABC34501DE35"));
            assertEquals(CNPJType.NUMERIC, CNPJ.getType("11222333000181"));
        }

        @Test
        @DisplayName("deve retornar false para nulo em isAlphanumeric e isNumeric")
        void shouldReturnFalseForNull() {
            assertFalse(CNPJ.isAlphanumeric(null));
            assertFalse(CNPJ.isNumeric(null));
        }

        @Test
        @DisplayName("deve lançar exceção em getType para nulo")
        void shouldThrowForNullType() {
            assertThrows(CNPJException.class, () -> CNPJ.getType(null));
        }
    }

    @Nested
    @DisplayName("parse e extração")
    class Parsing {

        @Test
        @DisplayName("deve decompor CNPJ alfanumérico")
        void shouldParseAlphanumeric() {
            CNPJParts parts = CNPJ.parse("12.ABC.345/01DE-35");
            assertEquals("12ABC345", parts.getRoot());
            assertEquals("01DE", parts.getBranch());
            assertEquals("35", parts.getCheckDigits());
            assertFalse(parts.isHeadquarters());
            assertEquals(CNPJType.ALPHANUMERIC, parts.getType());
        }

        @Test
        @DisplayName("deve decompor CNPJ numérico de matriz")
        void shouldParseNumericHeadquarters() {
            CNPJParts parts = CNPJ.parse("11222333000181");
            assertEquals("11222333", parts.getRoot());
            assertEquals("0001", parts.getBranch());
            assertEquals("81", parts.getCheckDigits());
            assertTrue(parts.isHeadquarters());
            assertEquals(CNPJType.NUMERIC, parts.getType());
        }

        @Test
        @DisplayName("deve extrair raiz via método estático")
        void shouldExtractRoot() {
            assertEquals("12ABC345", CNPJ.getRoot("12ABC34501DE35"));
        }

        @Test
        @DisplayName("deve extrair ordem via método estático")
        void shouldExtractBranch() {
            assertEquals("01DE", CNPJ.getBranch("12ABC34501DE35"));
        }

        @Test
        @DisplayName("deve extrair DVs via método estático")
        void shouldExtractCheckDigits() {
            assertEquals("35", CNPJ.getCheckDigits("12ABC34501DE35"));
        }

        @Test
        @DisplayName("deve verificar matriz via método estático")
        void shouldCheckHeadquarters() {
            assertTrue(CNPJ.isHeadquarters("11222333000181"));
            assertFalse(CNPJ.isHeadquarters("12ABC34501DE35"));
        }
    }

    @Nested
    @DisplayName("CNPJParts")
    class PartsTest {

        @Test
        @DisplayName("toUnformatted deve retornar CNPJ completo sem máscara")
        void shouldReturnUnformatted() {
            CNPJParts parts = CNPJ.parse("12ABC34501DE35");
            assertEquals("12ABC34501DE35", parts.toUnformatted());
        }

        @Test
        @DisplayName("toFormatted deve retornar CNPJ completo com máscara")
        void shouldReturnFormatted() {
            CNPJParts parts = CNPJ.parse("12ABC34501DE35");
            assertEquals("12.ABC.345/01DE-35", parts.toFormatted());
        }

        @Test
        @DisplayName("equals e hashCode")
        void shouldImplementEqualsAndHashCode() {
            CNPJParts a = CNPJ.parse("12ABC34501DE35");
            CNPJParts b = CNPJ.parse("12.ABC.345/01DE-35");
            assertEquals(a, b);
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        @DisplayName("toString deve conter informações")
        void shouldHaveToString() {
            CNPJParts parts = CNPJ.parse("12ABC34501DE35");
            String str = parts.toString();
            assertTrue(str.contains("12ABC345"));
            assertTrue(str.contains("01DE"));
            assertTrue(str.contains("35"));
        }
    }

    @Nested
    @DisplayName("regex patterns")
    class Patterns {

        @Test
        @DisplayName("getPattern deve aceitar CNPJ alfanumérico sem formatação")
        void shouldMatchUnformattedAlphanumeric() {
            Pattern p = CNPJ.getPattern();
            assertTrue(p.matcher("12ABC34501DE35").matches());
            assertFalse(p.matcher("12.ABC.345/01DE-35").matches());
        }

        @Test
        @DisplayName("getFormattedPattern deve aceitar CNPJ formatado")
        void shouldMatchFormatted() {
            Pattern p = CNPJ.getFormattedPattern();
            assertTrue(p.matcher("12.ABC.345/01DE-35").matches());
            assertFalse(p.matcher("12ABC34501DE35").matches());
        }

        @Test
        @DisplayName("getLegacyPattern deve aceitar apenas numéricos")
        void shouldMatchLegacy() {
            Pattern p = CNPJ.getLegacyPattern();
            assertTrue(p.matcher("11222333000181").matches());
            assertFalse(p.matcher("12ABC34501DE35").matches());
        }

        @Test
        @DisplayName("getLegacyFormattedPattern deve aceitar numérico formatado")
        void shouldMatchLegacyFormatted() {
            Pattern p = CNPJ.getLegacyFormattedPattern();
            assertTrue(p.matcher("11.222.333/0001-81").matches());
            assertFalse(p.matcher("12.ABC.345/01DE-35").matches());
        }
    }
}
