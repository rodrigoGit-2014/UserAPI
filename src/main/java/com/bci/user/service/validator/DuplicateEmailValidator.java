package com.bci.user.service.validator;

import com.bci.user.dto.UserRequestDto;
import com.bci.user.exception.EmailAlreadyExistsException;
import com.bci.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DuplicateEmailValidator implements IUserValidator {
    private final UserRepository userRepository;

    @Override
    public void validate(UserRequestDto request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new EmailAlreadyExistsException("Correo ya registrado");
        }
    }
}
