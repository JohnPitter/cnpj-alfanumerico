package io.github.johnpitter.cnpj;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CNPJ v1.1.0 — New Features")
class CNPJNewFeaturesTest {

    // ========================================================================
    // equals / isSameCompany
    // ========================================================================

    @Nested
    @DisplayName("equals")
    class Equals {

        @Test
        @DisplayName("deve ser igual ignorando formatação")
        void shouldEqualIgnoringFormat() {
            assertTrue(CNPJ.equals("12.ABC.345/01DE-35", "12ABC34501DE35"));
        }

        @Test
        @DisplayName("deve ser igual ignorando case")
        void shouldEqualIgnoringCase() {
            assertTrue(CNPJ.equals("12abc34501de35", "12ABC34501DE35"));
        }

        @Test
        @DisplayName("deve ser diferente para CNPJs distintos")
        void shouldNotEqualDifferentCnpjs() {
            assertFalse(CNPJ.equals("12ABC34501DE35", "11222333000181"));
        }

        @Test
        @DisplayName("deve retornar false para null")
        void shouldReturnFalseForNull() {
            assertFalse(CNPJ.equals(null, "12ABC34501DE35"));
            assertFalse(CNPJ.equals("12ABC34501DE35", null));
            assertFalse(CNPJ.equals(null, null));
        }
    }

    @Nested
    @DisplayName("isSameCompany")
    class IsSameCompany {

        @Test
        @DisplayName("deve detectar mesma empresa (mesma raiz)")
        void shouldDetectSameCompany() {
            String matriz = CNPJ.generateFromParts("12ABC345", "0001");
            String filial = CNPJ.generateFromParts("12ABC345", "0002");
            assertTrue(CNPJ.isSameCompany(matriz, filial));
        }

        @Test
        @DisplayName("deve detectar empresas diferentes")
        void shouldDetectDifferentCompanies() {
            assertFalse(CNPJ.isSameCompany("12ABC34501DE35", "11222333000181"));
        }

        @Test
        @DisplayName("deve retornar false para null")
        void shouldReturnFalseForNull() {
            assertFalse(CNPJ.isSameCompany(null, "12ABC34501DE35"));
        }
    }

    // ========================================================================
    // Storage migration
    // ========================================================================

    @Nested
    @DisplayName("storage migration")
    class StorageMigration {

        @Test
        @DisplayName("alfanumérico requer migração")
        void alphanumericRequiresMigration() {
            assertTrue(CNPJ.requiresStorageMigration("12ABC34501DE35"));
        }

        @Test
        @DisplayName("numérico não requer migração")
        void numericDoesNotRequireMigration() {
            assertFalse(CNPJ.requiresStorageMigration("11222333000181"));
        }

        @Test
        @DisplayName("getMinColumnType retorna CHAR(14) para alfanumérico")
        void shouldReturnCharForAlphanumeric() {
            assertEquals("CHAR(14)", CNPJ.getMinColumnType("12ABC34501DE35"));
        }

        @Test
        @DisplayName("getMinColumnType retorna BIGINT para numérico")
        void shouldReturnBigintForNumeric() {
            assertEquals("BIGINT", CNPJ.getMinColumnType("11222333000181"));
        }
    }

    // ========================================================================
    // Máscara LGPD
    // ========================================================================

    @Nested
    @DisplayName("mask (LGPD)")
    class Mask {

        @Test
        @DisplayName("deve mascarar no formato padrão")
        void shouldMaskDefault() {
            assertEquals("12.***.*/01DE-**", CNPJ.mask("12ABC34501DE35").substring(0, 2) + ".***.*/01DE-**");
            String masked = CNPJ.mask("12ABC34501DE35");
            assertTrue(masked.startsWith("12."));
            assertTrue(masked.endsWith("-**"));
            assertTrue(masked.contains("***"));
        }

        @Test
        @DisplayName("deve mascarar com N caracteres visíveis")
        void shouldMaskWithVisibleChars() {
            String masked = CNPJ.mask("12ABC34501DE35", 4);
            assertEquals("12AB**********", masked);
        }

        @Test
        @DisplayName("deve mascarar com 1 caractere visível")
        void shouldMaskWithOneChar() {
            String masked = CNPJ.mask("12ABC34501DE35", 1);
            assertEquals("1*************", masked);
        }

        @Test
        @DisplayName("deve mascarar com 12 caracteres visíveis")
        void shouldMaskWithAllVisible() {
            String masked = CNPJ.mask("12ABC34501DE35", 12);
            assertEquals("12ABC34501DE**", masked);
        }

        @Test
        @DisplayName("deve lançar exceção para visibleChars inválido")
        void shouldThrowForInvalidVisibleChars() {
            assertThrows(CNPJException.class, () -> CNPJ.mask("12ABC34501DE35", 0));
            assertThrows(CNPJException.class, () -> CNPJ.mask("12ABC34501DE35", 13));
        }

        @Test
        @DisplayName("deve aceitar CNPJ formatado")
        void shouldAcceptFormattedInput() {
            String masked = CNPJ.mask("12.ABC.345/01DE-35", 4);
            assertEquals("12AB**********", masked);
        }
    }

    // ========================================================================
    // Validação em lote
    // ========================================================================

    @Nested
    @DisplayName("validateBatch")
    class ValidateBatch {

        @Test
        @DisplayName("deve classificar válidos e inválidos")
        void shouldClassifyValidAndInvalid() {
            List<String> cnpjs = Arrays.asList(
                    "12ABC34501DE35",      // válido alfanumérico
                    "11222333000181",       // válido numérico
                    "00000000000000",       // inválido — todos iguais
                    "INVALIDO"              // inválido — formato
            );

            CNPJBatchResult result = CNPJ.validateBatch(cnpjs);

            assertEquals(4, result.getTotal());
            assertEquals(2, result.getValidCount());
            assertEquals(2, result.getInvalidCount());
            assertEquals(1, result.getNumericCount());
            assertEquals(1, result.getAlphanumericCount());
            assertFalse(result.isAllValid());
            assertTrue(result.hasAlphanumericEntries());
        }

        @Test
        @DisplayName("deve retornar tudo válido quando todos são válidos")
        void shouldReturnAllValid() {
            List<String> cnpjs = Arrays.asList(
                    "12ABC34501DE35",
                    "11222333000181"
            );

            CNPJBatchResult result = CNPJ.validateBatch(cnpjs);
            assertTrue(result.isAllValid());
            assertEquals(2, result.getValidCount());
        }

        @Test
        @DisplayName("deve funcionar com lista vazia")
        void shouldHandleEmptyList() {
            CNPJBatchResult result = CNPJ.validateBatch(Collections.<String>emptyList());
            assertEquals(0, result.getTotal());
            assertTrue(result.isAllValid());
        }

        @Test
        @DisplayName("deve lançar exceção para null")
        void shouldThrowForNull() {
            assertThrows(CNPJException.class, () -> CNPJ.validateBatch(null));
        }

        @Test
        @DisplayName("invalid map deve conter motivos do erro")
        void shouldContainErrorReasons() {
            List<String> cnpjs = Arrays.asList("INVALIDO");
            CNPJBatchResult result = CNPJ.validateBatch(cnpjs);
            assertFalse(result.getInvalid().isEmpty());
            assertNotNull(result.getInvalid().get("INVALIDO"));
        }

        @Test
        @DisplayName("toString deve conter informações úteis")
        void toStringShouldBeInformative() {
            CNPJBatchResult result = CNPJ.validateBatch(Arrays.asList("12ABC34501DE35"));
            String str = result.toString();
            assertTrue(str.contains("total=1"));
            assertTrue(str.contains("valid=1"));
        }
    }

    // ========================================================================
    // Código de barras
    // ========================================================================

    @Nested
    @DisplayName("getBarcodeEncoding")
    class GetBarcodeEncoding {

        @Test
        @DisplayName("deve retornar CODE_128A para alfanumérico")
        void shouldReturnCode128AForAlphanumeric() {
            assertEquals(BarcodeEncoding.CODE_128A, CNPJ.getBarcodeEncoding("12ABC34501DE35"));
        }

        @Test
        @DisplayName("deve retornar CODE_128C para numérico")
        void shouldReturnCode128CForNumeric() {
            assertEquals(BarcodeEncoding.CODE_128C, CNPJ.getBarcodeEncoding("11222333000181"));
        }

        @Test
        @DisplayName("deve lançar exceção para null")
        void shouldThrowForNull() {
            assertThrows(CNPJException.class, () -> CNPJ.getBarcodeEncoding(null));
        }
    }

    // ========================================================================
    // Listagem de filiais
    // ========================================================================

    @Nested
    @DisplayName("listBranches")
    class ListBranches {

        @Test
        @DisplayName("deve gerar CNPJs válidos para cada filial")
        void shouldGenerateValidCnpjsForBranches() {
            List<String> branches = CNPJ.listBranches("12ABC345", Arrays.asList("0001", "0002", "01DE"));

            assertEquals(3, branches.size());
            for (String cnpj : branches) {
                assertTrue(CNPJ.isValid(cnpj), "CNPJ gerado deve ser válido: " + cnpj);
                assertEquals("12ABC345", CNPJ.getRoot(cnpj));
            }
        }

        @Test
        @DisplayName("primeiro deve ser matriz (0001)")
        void firstShouldBeHeadquarters() {
            List<String> branches = CNPJ.listBranches("12ABC345", Arrays.asList("0001", "0002"));
            assertTrue(CNPJ.isHeadquarters(branches.get(0)));
            assertFalse(CNPJ.isHeadquarters(branches.get(1)));
        }

        @Test
        @DisplayName("deve lançar exceção para raiz inválida")
        void shouldThrowForInvalidRoot() {
            assertThrows(CNPJException.class,
                    () -> CNPJ.listBranches("123", Arrays.asList("0001")));
        }

        @Test
        @DisplayName("deve lançar exceção para branches null")
        void shouldThrowForNullBranches() {
            assertThrows(CNPJException.class,
                    () -> CNPJ.listBranches("12ABC345", null));
        }
    }
}
