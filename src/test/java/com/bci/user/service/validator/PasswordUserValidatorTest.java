package com.bci.user.service.validator;

import com.bci.user.dto.UserRequestDto;
import com.bci.user.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUserValidatorTest {
    private PasswordUserValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordUserValidator();
        ReflectionTestUtils.setField(validator, "passwordRegex", "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$");
    }

    @Test
    @DisplayName("Debe lanzar BusinessException si la contraseña no cumple el formato")
    void shouldThrowExceptionWhenPasswordIsInvalid() {
        // given
        UserRequestDto request = UserRequestDto.builder()
                .password("abc") // inválida
                .build();

        // when / then
        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> validator.validate(request)
        );

        assertEquals("La contraseña no cumple el formato requerido", ex.getMessage());
    }

    @Test
    @DisplayName("No debe lanzar excepción si la contraseña cumple el formato")
    void shouldPassWhenPasswordIsValid() {
        // given
        UserRequestDto request = UserRequestDto.builder()
                .password("Abcdef12")
                .build();

        // when / then
        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    @DisplayName("Debe lanzar BusinessException si la contraseña es nula o vacía")
    void shouldThrowExceptionWhenPasswordIsNullOrEmpty() {
        UserRequestDto nullPwd = UserRequestDto.builder()
                .password("11")
                .build();

        BusinessException ex1 = assertThrows(
                BusinessException.class,
                () -> validator.validate(nullPwd)
        );
        assertEquals("La contraseña no cumple el formato requerido", ex1.getMessage());

        UserRequestDto emptyPwd = UserRequestDto.builder()
                .password("")
                .build();

        BusinessException ex2 = assertThrows(
                BusinessException.class,
                () -> validator.validate(emptyPwd)
        );
        assertEquals("La contraseña no cumple el formato requerido", ex2.getMessage());
    }
}