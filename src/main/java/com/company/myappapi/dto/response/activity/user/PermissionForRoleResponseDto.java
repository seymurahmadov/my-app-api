package com.company.myappapi.dto.response.activity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionForRoleResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean granted;
}
