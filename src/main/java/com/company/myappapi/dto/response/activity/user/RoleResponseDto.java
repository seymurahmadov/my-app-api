package com.company.myappapi.dto.response.activity.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoleResponseDto {

    private Long id;

    private String name;

    public RoleResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

