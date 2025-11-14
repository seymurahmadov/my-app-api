package com.company.myappapi.mapper.user;

import com.company.myappapi.dto.request.user.PermissionRequestDto;
import com.company.myappapi.dto.response.activity.user.PermissionResponseDto;
import com.company.myappapi.entity.user.Permission;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PermissionMapper {
    PermissionResponseDto toPermissionResponseDto(Permission permission);
    Permission toPermission(PermissionRequestDto permissionRequestDto);
    void updatePermission(PermissionRequestDto requestDto, @MappingTarget Permission permission);
}