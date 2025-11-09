package com.bci.user.controller;


import com.bci.user.dto.PhoneRequestDto;
import com.bci.user.dto.PhoneResponseDto;
import com.bci.user.dto.UserRequestDto;
import com.bci.user.dto.UserResponseDto;
import com.bci.user.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private IUserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;
    private UserRequestDto requestDto;
    private UserResponseDto responseDto;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        requestDto = UserRequestDto.builder()
                .name("Juan Rodriguez")
                .email("juan@rodriguez.org")
                .password("Hunter2024")
                .phones(List.of(
                        PhoneRequestDto.builder()
                                .number("1234567")
                                .citycode("1")
                                .contrycode("57")
                                .build()))
                .build();

        responseDto = UserResponseDto.builder()
                .id(UUID.randomUUID())
                .name("Juan Rodriguez")
                .email("juan@rodriguez.org")
                .phones(oonvertTo(requestDto.getPhones()))
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .token(UUID.randomUUID().toString())
                .active(true)
                .build();
    }

    List<PhoneResponseDto> oonvertTo(List<PhoneRequestDto> phones) {
        List<PhoneResponseDto> phoneResponseDtos = new ArrayList<>();
        phones.forEach(s -> phoneResponseDtos.add(PhoneResponseDto.builder()
                .number(s.getNumber())
                .citycode(s.getCitycode())
                .contrycode(s.getContrycode())
                .build()));
        return phoneResponseDtos;
    }

    @Test
    @DisplayName("POST /api/v1/users - debe registrar un usuario y devolver 201")
    void registerUser_ShouldReturnCreated() throws Exception {
        when(userService.register(any(UserRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("juan@rodriguez.org"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("GET /api/v1/users/{id} - debe devolver usuario existente")
    void getUserById_ShouldReturnOk() throws Exception {
        UUID userId = responseDto.getId();
        when(userService.findById(eq(userId))).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.email").value("juan@rodriguez.org"));
    }

    @Test
    @DisplayName("POST /api/v1/users - debe devolver 400 si la entrada es inválida")
    void registerUser_ShouldReturnBadRequest_WhenInvalid() throws Exception {
        UserRequestDto invalidRequest = UserRequestDto.builder()
                .name("") // inválido
                .email("correo-invalido")
                .password("")
                .phones(List.of())
                .build();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}