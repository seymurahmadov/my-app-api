package com.company.myappapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SignInRequestDto implements Serializable {

    @SuppressWarnings({"all"})
    private static final long serialVersionUID = 5926468583005150707L;

    @Email(message = "Zəhmət olmasa düzgün email formatı daxil edin")
    @NotBlank(message = "Email boş ola bilməz")
    @NotNull(message = "Email boş ola bilməz")
    @Size(min = 5, max = 50, message = "Email 5-50 simvol arası olmalıdır")
    private String email;

    @NotBlank(message = "Şifrə boş ola bilməz")
    @NotNull(message = "Şifrə boş ola bilməz")
    private String password;
}
