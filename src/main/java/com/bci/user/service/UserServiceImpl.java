package com.bci.user.service;

import com.bci.user.dto.UserRequestDto;
import com.bci.user.dto.UserResponseDto;
import com.bci.user.exception.BusinessException;
import com.bci.user.mappers.UserMapper;
import com.bci.user.persistence.entity.UserEntity;
import com.bci.user.persistence.repository.UserRepository;
import com.bci.user.service.validator.IUserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final List<IUserValidator> userRequestValidators;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRequestDto request) {

        userRequestValidators.forEach(ur -> ur.validate(request));

        UserEntity user = userMapper.toEntity(request);
        UserEntity saved = userRepository.save(user);

        return userMapper.toResponse(saved);

    }

    @Override
    public UserResponseDto findById(UUID id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));
        return userMapper.toResponse(user);
    }


}
