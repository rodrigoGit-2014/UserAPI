package com.bci.user.controller;

import com.bci.user.dto.UserRequestDto;
import com.bci.user.dto.UserResponseDto;
import com.bci.user.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@Valid @RequestBody UserRequestDto request) {
        return userService.register(request);
    }

    @GetMapping("/{id}")
    public UserResponseDto getById(@PathVariable UUID id) {
        return userService.findById(id);
    }
}
