package io.github.johnpitter.cnpj.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bean Validation — CNPJValidator")
class CNPJValidatorTest {

    @Test
    @DisplayName("deve retornar true para null (validação nullable)")
    void shouldReturnTrueForNull() {
        assertTrue(CNPJValidator.isValid(null));
    }

    @Test
    @DisplayName("deve retornar true para CNPJ válido")
    void shouldReturnTrueForValidCnpj() {
        assertTrue(CNPJValidator.isValid("12ABC34501DE35"));
    }

    @Test
    @DisplayName("deve retornar true para CNPJ numérico válido")
    void shouldReturnTrueForValidNumericCnpj() {
        assertTrue(CNPJValidator.isValid("11222333000181"));
    }

    @Test
    @DisplayName("deve retornar false para CNPJ inválido")
    void shouldReturnFalseForInvalidCnpj() {
        assertFalse(CNPJValidator.isValid("INVALIDO"));
    }

    @Test
    @DisplayName("deve retornar false para tipo não-String")
    void shouldReturnFalseForNonString() {
        assertFalse(CNPJValidator.isValid(12345));
    }

    @Test
    @DisplayName("deve retornar true para CNPJ formatado válido")
    void shouldReturnTrueForFormattedValid() {
        assertTrue(CNPJValidator.isValid("12.ABC.345/01DE-35"));
    }
}
