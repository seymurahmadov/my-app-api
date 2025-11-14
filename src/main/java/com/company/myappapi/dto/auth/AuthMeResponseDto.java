package com.company.myappapi.dto.auth;


import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AuthMeResponseDto {
    private Long id;
    private String email;
    private String roleName;
    private Set<String> permissions;
    private Boolean isGmOrHr;
}