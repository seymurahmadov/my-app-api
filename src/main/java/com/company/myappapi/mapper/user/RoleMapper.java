package com.company.myappapi.mapper.user;

import com.company.myappapi.dto.request.user.RoleRequestDto;
import com.company.myappapi.dto.response.activity.user.RoleResponseDto;
import com.company.myappapi.entity.user.Role;
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
public interface RoleMapper {

    RoleResponseDto toRoleResponseDto(Role role);
    Role toRole(RoleRequestDto roleResponseDto);
    void updateRole(RoleRequestDto roleRequestDto, @MappingTarget Role role);
}
