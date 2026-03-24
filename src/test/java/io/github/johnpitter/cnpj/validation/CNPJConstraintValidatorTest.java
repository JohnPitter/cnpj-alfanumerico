package io.github.johnpitter.cnpj.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bean Validation — CNPJConstraintValidator")
class CNPJConstraintValidatorTest {

    @Test
    @DisplayName("deve retornar true para null (validação nullable)")
    void shouldReturnTrueForNull() {
        assertTrue(CNPJConstraintValidator.isValid(null));
    }

    @Test
    @DisplayName("deve retornar true para CNPJ válido")
    void shouldReturnTrueForValidCnpj() {
        assertTrue(CNPJConstraintValidator.isValid("12ABC34501DE35"));
    }

    @Test
    @DisplayName("deve retornar true para CNPJ numérico válido")
    void shouldReturnTrueForValidNumericCnpj() {
        assertTrue(CNPJConstraintValidator.isValid("11222333000181"));
    }

    @Test
    @DisplayName("deve retornar false para CNPJ inválido")
    void shouldReturnFalseForInvalidCnpj() {
        assertFalse(CNPJConstraintValidator.isValid("INVALIDO"));
    }

    @Test
    @DisplayName("deve retornar false para tipo não-String")
    void shouldReturnFalseForNonString() {
        assertFalse(CNPJConstraintValidator.isValid(12345));
    }

    @Test
    @DisplayName("deve retornar true para CNPJ formatado válido")
    void shouldReturnTrueForFormattedValid() {
        assertTrue(CNPJConstraintValidator.isValid("12.ABC.345/01DE-35"));
    }
}
