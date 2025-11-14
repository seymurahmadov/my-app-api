package com.company.myappapi.dto.response.activity.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PermissionResponseDto {
    private Long id;
    private String name;
    private String description;

    public PermissionResponseDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

}
