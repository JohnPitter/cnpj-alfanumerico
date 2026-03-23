package io.github.joaop.cnpj;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CNPJGenerator")
class CNPJGeneratorTest {

    @Nested
    @DisplayName("generate")
    class Generate {

        @RepeatedTest(50)
        @DisplayName("deve gerar CNPJ alfanumérico válido")
        void shouldGenerateValidAlphanumeric() {
            String cnpj = CNPJGenerator.generate();
            assertEquals(14, cnpj.length());
            assertTrue(CNPJ.isValid(cnpj), "CNPJ gerado deve ser válido: " + cnpj);
            assertTrue(CNPJ.isAlphanumeric(cnpj), "CNPJ deve ser alfanumérico: " + cnpj);
        }
    }

    @Nested
    @DisplayName("generateFormatted")
    class GenerateFormatted {

        @RepeatedTest(10)
        @DisplayName("deve gerar CNPJ alfanumérico formatado válido")
        void shouldGenerateFormattedValid() {
            String cnpj = CNPJGenerator.generateFormatted();
            assertTrue(CNPJFormatter.isFormatted(cnpj), "CNPJ deve estar formatado: " + cnpj);
            assertTrue(CNPJ.isValid(cnpj), "CNPJ gerado deve ser válido: " + cnpj);
        }
    }

    @Nested
    @DisplayName("generateNumeric")
    class GenerateNumeric {

        @RepeatedTest(50)
        @DisplayName("deve gerar CNPJ numérico válido")
        void shouldGenerateValidNumeric() {
            String cnpj = CNPJGenerator.generateNumeric();
            assertEquals(14, cnpj.length());
            assertTrue(CNPJ.isValid(cnpj), "CNPJ gerado deve ser válido: " + cnpj);
            assertTrue(CNPJ.isNumeric(cnpj), "CNPJ deve ser numérico: " + cnpj);
        }
    }

    @Nested
    @DisplayName("generateFromParts")
    class GenerateFromParts {

        @Test
        @DisplayName("deve gerar CNPJ com raiz e ordem específicas")
        void shouldGenerateFromRootAndBranch() {
            String cnpj = CNPJGenerator.generateFromParts("12ABC345", "01DE");
            assertTrue(CNPJ.isValid(cnpj));
            assertEquals("12ABC345", CNPJ.getRoot(cnpj));
            assertEquals("01DE", CNPJ.getBranch(cnpj));
        }

        @Test
        @DisplayName("deve lançar exceção para raiz com tamanho errado")
        void shouldThrowForInvalidRoot() {
            assertThrows(CNPJException.class,
                    () -> CNPJGenerator.generateFromParts("123", "0001"));
        }

        @Test
        @DisplayName("deve lançar exceção para ordem com tamanho errado")
        void shouldThrowForInvalidBranch() {
            assertThrows(CNPJException.class,
                    () -> CNPJGenerator.generateFromParts("12345678", "01"));
        }
    }

    @Nested
    @DisplayName("generateFromBase")
    class GenerateFromBase {

        @Test
        @DisplayName("deve gerar CNPJ a partir de base de 12 caracteres")
        void shouldGenerateFromBase() {
            String cnpj = CNPJGenerator.generateFromBase("12ABC34501DE");
            assertEquals("12ABC34501DE35", cnpj);
            assertTrue(CNPJ.isValid(cnpj));
        }

        @Test
        @DisplayName("deve lançar exceção para base inválida")
        void shouldThrowForInvalidBase() {
            assertThrows(CNPJException.class, () -> CNPJGenerator.generateFromBase("123"));
        }
    }

    @Nested
    @DisplayName("generateHeadquarters")
    class GenerateHeadquarters {

        @Test
        @DisplayName("deve gerar CNPJ de matriz (branch = 0001)")
        void shouldGenerateHeadquarters() {
            String cnpj = CNPJGenerator.generateHeadquarters("12ABC345");
            assertTrue(CNPJ.isValid(cnpj));
            assertTrue(CNPJ.isHeadquarters(cnpj));
            assertEquals("0001", CNPJ.getBranch(cnpj));
        }
    }
}
