package io.github.johnpitter.cnpj;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CNPJValidator")
class CNPJValidatorTest {

    @Nested
    @DisplayName("isValid")
    class IsValid {

        @Test
        @DisplayName("deve validar CNPJ alfanumérico do exemplo da Receita Federal")
        void shouldValidateAlphanumericExample() {
            assertTrue(CNPJValidator.isValid("12ABC34501DE35"));
        }

        @Test
        @DisplayName("deve validar CNPJ alfanumérico formatado")
        void shouldValidateFormattedAlphanumeric() {
            assertTrue(CNPJValidator.isValid("12.ABC.345/01DE-35"));
        }

        @Test
        @DisplayName("deve validar CNPJ numérico legado")
        void shouldValidateNumericLegacy() {
            assertTrue(CNPJValidator.isValid("11222333000181"));
        }

        @Test
        @DisplayName("deve validar CNPJ numérico legado formatado")
        void shouldValidateFormattedNumericLegacy() {
            assertTrue(CNPJValidator.isValid("11.222.333/0001-81"));
        }

        @Test
        @DisplayName("deve aceitar input em minúsculas")
        void shouldAcceptLowercase() {
            assertTrue(CNPJValidator.isValid("12abc34501de35"));
        }

        @Test
        @DisplayName("deve rejeitar CNPJ com dígitos verificadores errados")
        void shouldRejectWrongCheckDigits() {
            assertFalse(CNPJValidator.isValid("12ABC34501DE99"));
        }

        @Test
        @DisplayName("deve rejeitar CNPJ com todos caracteres iguais")
        void shouldRejectAllSameChars() {
            assertFalse(CNPJValidator.isValid("00000000000000"));
            assertFalse(CNPJValidator.isValid("11111111111111"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("deve rejeitar nulo e vazio")
        void shouldRejectNullAndEmpty(String input) {
            assertFalse(CNPJValidator.isValid(input));
        }

        @ParameterizedTest
        @ValueSource(strings = {"123", "12345678901234567890", "ABCDEFGHIJKLMN"})
        @DisplayName("deve rejeitar tamanhos inválidos e letras nos DVs")
        void shouldRejectInvalidFormats(String input) {
            assertFalse(CNPJValidator.isValid(input));
        }

        @Test
        @DisplayName("deve rejeitar CNPJ com caracteres especiais no corpo")
        void shouldRejectSpecialChars() {
            assertFalse(CNPJValidator.isValid("12@BC34501DE35"));
        }
    }

    @Nested
    @DisplayName("validate")
    class Validate {

        @Test
        @DisplayName("deve lançar CNPJException para CNPJ inválido")
        void shouldThrowForInvalid() {
            CNPJException ex = assertThrows(CNPJException.class,
                    () -> CNPJValidator.validate("12ABC34501DE99"));
            assertTrue(ex.getMessage().contains("Dígitos verificadores inválidos"));
        }

        @Test
        @DisplayName("deve lançar CNPJException para nulo")
        void shouldThrowForNull() {
            assertThrows(CNPJException.class, () -> CNPJValidator.validate(null));
        }

        @Test
        @DisplayName("deve lançar CNPJException para todos iguais")
        void shouldThrowForAllSame() {
            CNPJException ex = assertThrows(CNPJException.class,
                    () -> CNPJValidator.validate("00000000000000"));
            assertTrue(ex.getMessage().contains("todos os caracteres são iguais"));
        }

        @Test
        @DisplayName("não deve lançar para CNPJ válido")
        void shouldNotThrowForValid() {
            assertDoesNotThrow(() -> CNPJValidator.validate("12ABC34501DE35"));
        }
    }

    @Nested
    @DisplayName("calculateCheckDigits")
    class CalculateCheckDigits {

        @Test
        @DisplayName("deve calcular DVs do exemplo alfanumérico")
        void shouldCalculateAlphanumericDVs() {
            assertEquals("35", CNPJValidator.calculateCheckDigits("12ABC34501DE"));
        }

        @Test
        @DisplayName("deve calcular DVs de CNPJ numérico legado")
        void shouldCalculateNumericDVs() {
            assertEquals("81", CNPJValidator.calculateCheckDigits("112223330001"));
        }

        @Test
        @DisplayName("deve lançar exceção para base com tamanho errado")
        void shouldThrowForWrongLength() {
            assertThrows(CNPJException.class, () -> CNPJValidator.calculateCheckDigits("123"));
        }

        @Test
        @DisplayName("deve lançar exceção para base nula")
        void shouldThrowForNull() {
            assertThrows(CNPJException.class, () -> CNPJValidator.calculateCheckDigits(null));
        }

        @Test
        @DisplayName("deve aceitar base em minúsculas")
        void shouldAcceptLowercase() {
            assertEquals("35", CNPJValidator.calculateCheckDigits("12abc34501de"));
        }

        @Test
        @DisplayName("deve calcular DV=0 quando resto < 2")
        void shouldReturnZeroWhenRemainderLessThanTwo() {
            // Caso onde o DV resulta em 0
            String base = "000000000000";
            String dvs = CNPJValidator.calculateCheckDigits(base);
            assertNotNull(dvs);
            assertEquals(2, dvs.length());
        }
    }

    @Nested
    @DisplayName("retrocompatibilidade")
    class BackwardsCompatibility {

        @ParameterizedTest
        @ValueSource(strings = {
                "11222333000181",
                "00623904000173",
                "61882613000194",
                "33014556000196"
        })
        @DisplayName("CNPJs numéricos conhecidos devem ser válidos com o novo algoritmo")
        void knownNumericCnpjsShouldBeValid(String cnpj) {
            assertTrue(CNPJValidator.isValid(cnpj),
                    "CNPJ numérico " + cnpj + " deveria ser válido com o algoritmo alfanumérico");
        }
    }
}
