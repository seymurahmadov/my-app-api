package com.company.myappapi.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RolePermissionUpdateRequestDto {
    @JsonIgnore
    private Long id;

    private Set<Long> permissionIds;
}
