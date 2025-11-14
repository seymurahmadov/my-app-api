package com.company.myappapi.mapper.user;

import com.company.myappapi.dto.request.user.UserRequestDto;
import com.company.myappapi.dto.response.activity.user.UserCoefficientResponseDto;
import com.company.myappapi.dto.response.activity.user.UserHierarchyResponse;
import com.company.myappapi.dto.response.activity.user.UserResponseDto;
import com.company.myappapi.entity.user.Role;
import com.company.myappapi.entity.user.User;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UserMapper {

    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.name", target = "roleName")
    UserResponseDto toUserResponseDto(User user);

    @Mapping(target = "name", expression = "java(user.getName() + \" \" + user.getSurname())")
    UserHierarchyResponse toUserHierarchyResponseDto(User user);

    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.name", target = "roleName")
    UserCoefficientResponseDto toUserCoefficientResponseDto(User user);

    @Mapping(source = "roleId",target = "role", qualifiedByName = "mapRole")
    User toUser(UserRequestDto userRequestDto);

    @Mapping(source = "roleId",target = "role", qualifiedByName = "mapRole")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(UserRequestDto userRequestDto, @MappingTarget User user);

    @Named("mapRole")
    default Role mapRole(Long id){
        return new Role(id);
    }
}
