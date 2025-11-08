package com.bci.user.service;

import com.bci.user.dto.UserRequestDto;
import com.bci.user.dto.UserResponseDto;

import java.util.UUID;

public interface IUserService {

    UserResponseDto register(UserRequestDto request);

    UserResponseDto findById(UUID id);
}
