package com.bci.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PhoneResponseDto {
    private String number;
    private String citycode;
    private String contrycode;
}
