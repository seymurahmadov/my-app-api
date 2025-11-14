package com.company.myappapi.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionRequestDto {
    @JsonIgnore
    private Long id;

    @NotNull(message = "İcazə adı boş ola bilməz")
    @NotBlank(message = "İcazə adı daxil edilməlidir")
    private String name;

    @NotNull(message = "İcazə haqqında məlumat boş ola bilməz")
    @NotBlank(message = "İcazə haqqında məlumat daxil edilməlidir")
    private String description;
}
