package com.company.myappapi.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRequestDto {

    @NotNull(message = "Ad sahəsi boş ola bilməz!")
    @NotBlank(message = "Ad sahəsi boş ola bilməz!")
    private String name;

    @NotNull(message = "Soyad sahəsi boş ola bilməz!")
    @NotBlank(message = "Soyad sahəsi boş ola bilməz!")
    private String surname;

    @NotNull(message = "Email sahəsi boş ola bilməz!")
    @NotBlank(message = "Email sahəsi boş ola bilməz!")
    @Email(message = "Zəhmət olmasa düzgün email formatı daxil edin")
    @Size(min = 5, max = 50, message = "Email 5-50 simvol arası olmalıdır")
    private String email;

    @NotNull(message = "İşə başlama tarixi boş ola bilməz")
    private LocalDate employmentDate;

    @NotNull(message = "Rol boş ola bilməz!")
    private Long roleId;
}
