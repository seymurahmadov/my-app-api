package com.company.myappapi.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordDto {

    @NotNull(message = "Email boş ola bilməz")
    @NotBlank(message = "Email boş ola bilməz")
    @Size(min = 1, max = 100, message = "Email uzunluğu {min}-{max} simvol arası olmalıdır")
    @Email(message = "Zəhmət olmasa doğru email daxil edin")
    private String email;
}
