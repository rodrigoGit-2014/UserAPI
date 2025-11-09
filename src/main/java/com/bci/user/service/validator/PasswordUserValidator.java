package com.bci.user.service.validator;

import com.bci.user.dto.UserRequestDto;
import com.bci.user.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class PasswordUserValidator implements IUserValidator {
    @Value("${app.security.password-regex}")
    private String passwordRegex;

    @Override
    public void validate(UserRequestDto request) {
        if (!Pattern.matches(passwordRegex, request.getPassword())) {
            throw new BusinessException("La contraseña no cumple el formato requerido");
        }
    }
}
