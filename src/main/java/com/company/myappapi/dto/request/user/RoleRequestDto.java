package com.company.myappapi.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleRequestDto {

    @NotNull(message = "Rol boş ola bilməz")
    @NotBlank(message = "Rol daxil edilməlidir")
    private String name;
}
