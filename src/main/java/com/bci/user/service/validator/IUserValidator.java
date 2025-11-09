package com.bci.user.service.validator;

import com.bci.user.dto.UserRequestDto;

public interface IUserValidator {

    void validate(UserRequestDto request);
}
