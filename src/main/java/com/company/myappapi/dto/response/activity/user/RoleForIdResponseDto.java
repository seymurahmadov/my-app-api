package com.company.myappapi.dto.response.activity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleForIdResponseDto {
    private Long id;
    private String name;
    private List<PermissionForRoleResponseDto> permission;
    private Set<Long> selected;
}
