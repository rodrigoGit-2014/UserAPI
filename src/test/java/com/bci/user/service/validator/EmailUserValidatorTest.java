package com.bci.user.service.validator;

import com.bci.user.dto.UserRequestDto;
import com.bci.user.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class EmailUserValidatorTest {
    private EmailUserValidator validator;

    @BeforeEach
    void setUp() {
        validator = new EmailUserValidator();
        ReflectionTestUtils.setField(validator, "emailRegex", "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    @Test
    @DisplayName("Debe lanzar BusinessException cuando el email no cumple el formato")
    void shouldThrowExceptionWhenEmailIsInvalid() {
        // given
        UserRequestDto request = UserRequestDto.builder()
                .email("correo_invalido")
                .build();

        // when / then
        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> validator.validate(request)
        );

        assertEquals("El formato del correo no es válido", ex.getMessage());
    }

    @Test
    @DisplayName("No debe lanzar excepción cuando el email es válido")
    void shouldPassWhenEmailIsValid() {
        // given
        UserRequestDto request = UserRequestDto.builder()
                .email("usuario@dominio.com")
                .build();

        // when - no debe lanzar excepción
        assertDoesNotThrow(() -> validator.validate(request));
    }


}