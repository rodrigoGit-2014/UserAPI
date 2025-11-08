package com.bci.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneRequestDto {
    @NotBlank(message = "El número de teléfono es obligatorio")
    private String number;

    @NotBlank(message = "El código de ciudad es obligatorio")
    private String citycode;

    @NotBlank(message = "El código de país es obligatorio")
    private String contrycode;
}
