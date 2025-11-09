package com.bci.user.service.validator;

import com.bci.user.dto.UserRequestDto;
import com.bci.user.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class EmailUserValidator implements IUserValidator {
    @Value("${app.security.email-regex}")
    private String emailRegex;

    @Override
    public void validate(UserRequestDto request) {
        if (!Pattern.matches(emailRegex, request.getEmail())) {
            throw new BusinessException("El formato del correo no es válido");
        }
    }
}
