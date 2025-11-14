package com.company.myappapi.dto;

import com.company.myappapi.validator.ValidPassword;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {

    @NotBlank(message = "Şifrə boş ola bilməz")
    @NotNull(message = "Şifrə boş ola bilməz")
    @Size(min = 5, max = 30, message = "Şifrə {min}-{max} simvol arası olmalıdır")
    @ValidPassword
    private String oldPassword;

    @NotBlank(message = "Şifrə boş ola bilməz")
    @NotNull(message = "Şifrə boş ola bilməz")
    @Size(min = 5, max = 30, message = "Şifrə {min}-{max} simvol arası olmalıdır")
    @ValidPassword
    private String newPassword;

    @NotBlank(message = "Təsdiq şifrəsi boş ola bilməz")
    @NotNull(message = "Təsdiq şifrəsi boş ola bilməz")
    private String confirmPassword;

    @JsonIgnore
    @AssertTrue(message = "Yeni şifrə ilə təsdiq şifrəsi uyğun deyil!")
    public Boolean isPasswordsMatching() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }
}