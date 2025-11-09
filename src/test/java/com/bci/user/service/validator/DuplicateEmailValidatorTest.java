package com.bci.user.service.validator;

import com.bci.user.dto.UserRequestDto;
import com.bci.user.exception.EmailAlreadyExistsException;
import com.bci.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DuplicateEmailValidatorTest {
    @Mock
    private UserRepository userRepository;

    private DuplicateEmailValidator validator;

    @BeforeEach
    void setUp() {
        validator = new DuplicateEmailValidator(userRepository);
    }

    @Test
    @DisplayName("Debe lanzar EmailAlreadyExistsException si el correo ya existe")
    void shouldThrowExceptionWhenEmailExists() {
        // given
        UserRequestDto request = UserRequestDto.builder()
                .email("test@correo.com")
                .build();

        when(userRepository.existsByEmailIgnoreCase("test@correo.com"))
                .thenReturn(true);

        // when / then
        assertThrows(EmailAlreadyExistsException.class, () -> validator.validate(request));

        verify(userRepository).existsByEmailIgnoreCase("test@correo.com");
    }

    @Test
    @DisplayName("No debe lanzar excepción si el correo no existe")
    void shouldPassWhenEmailDoesNotExist() {
        // given
        UserRequestDto request = UserRequestDto.builder()
                .email("nuevo@correo.com")
                .build();

        when(userRepository.existsByEmailIgnoreCase("nuevo@correo.com"))
                .thenReturn(false);

        // when
        validator.validate(request);

        // then
        verify(userRepository).existsByEmailIgnoreCase("nuevo@correo.com");
        verifyNoMoreInteractions(userRepository);
    }
}