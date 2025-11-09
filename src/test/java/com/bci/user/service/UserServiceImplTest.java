package com.bci.user.service;

import com.bci.user.dto.UserRequestDto;
import com.bci.user.dto.UserResponseDto;
import com.bci.user.exception.BusinessException;
import com.bci.user.mappers.UserMapper;
import com.bci.user.persistence.entity.UserEntity;
import com.bci.user.persistence.repository.UserRepository;
import com.bci.user.service.validator.IUserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private IUserValidator validator1;

    @Mock
    private IUserValidator validator2;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        // inyectamos la lista de validadores manualmente
        List<IUserValidator> validators = List.of(validator1, validator2);
        userService = new UserServiceImpl(userRepository, validators, userMapper);
    }

    @Test
    @DisplayName("register() debe validar, mapear, guardar y devolver el UserResponseDto")
    void register_ShouldValidateMapSaveAndReturnResponse() {
        // given
        UserRequestDto request = UserRequestDto.builder()
                .name("Juan Rodriguez")
                .email("juan@rodriguez.org")
                .password("Hunter2024")
                .build();

        UserEntity mappedEntity = UserEntity.builder()
                .id(UUID.randomUUID())
                .name("Juan Rodriguez")
                .email("juan@rodriguez.org")
                .build();

        UserEntity savedEntity = UserEntity.builder()
                .id(mappedEntity.getId())
                .name(mappedEntity.getName())
                .email(mappedEntity.getEmail())
                .build();

        UserResponseDto responseDto = UserResponseDto.builder()
                .id(savedEntity.getId())
                .name(savedEntity.getName())
                .email(savedEntity.getEmail())
                .active(true)
                .build();

        when(userMapper.toEntity(request)).thenReturn(mappedEntity);
        when(userRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(userMapper.toResponse(savedEntity)).thenReturn(responseDto);

        // when
        UserResponseDto result = userService.register(request);

        // then
        // se llamaron los validadores
        verify(validator1).validate(request);
        verify(validator2).validate(request);

        // se mapeó, guardó y volvió a mapear
        verify(userMapper).toEntity(request);
        verify(userRepository).save(mappedEntity);
        verify(userMapper).toResponse(savedEntity);

        // y el resultado es el esperado
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(savedEntity.getId());
        assertThat(result.getEmail()).isEqualTo("juan@rodriguez.org");
        assertThat(result.isActive()).isTrue();
    }

    @Test
    @DisplayName("findById() debe devolver el usuario cuando existe")
    void findById_ShouldReturnUser_WhenExists() {
        // given
        UUID id = UUID.randomUUID();

        UserEntity userEntity = UserEntity.builder()
                .id(id)
                .name("Juan")
                .email("juan@rodriguez.org")
                .build();

        UserResponseDto responseDto = UserResponseDto.builder()
                .id(id)
                .name("Juan")
                .email("juan@rodriguez.org")
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        when(userMapper.toResponse(userEntity)).thenReturn(responseDto);

        // when
        UserResponseDto result = userService.findById(id);

        // then
        verify(userRepository).findById(id);
        verify(userMapper).toResponse(userEntity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getEmail()).isEqualTo("juan@rodriguez.org");
    }

    @Test
    @DisplayName("findById() debe lanzar BusinessException cuando el usuario no existe")
    void findById_ShouldThrowBusinessException_WhenNotFound() {
        // given
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // when - then
        BusinessException ex = assertThrows(BusinessException.class,
                () -> userService.findById(id));

        assertThat(ex.getMessage()).isEqualTo("Usuario no encontrado");

        verify(userRepository).findById(id);
        verifyNoInteractions(userMapper); // no debería mapear nada si no se encontró
    }
}