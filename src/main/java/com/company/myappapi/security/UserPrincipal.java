package com.company.myappapi.security;

import com.company.myappapi.entity.user.Permission;
import com.company.myappapi.entity.user.User;
import com.company.myappapi.util.Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class UserPrincipal implements UserDetails {
    private Long id;
    private String name;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal create(User user) {
        var perm = convertSysPermissions(user.getRole().getPermission());

        return UserPrincipal.builder()
                .id(user.getId())
                .name(Util.formatNameSurname(user.getName(), user.getSurname()))
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(perm)
                .build();
    }

    public static UserPrincipal create(Long id) {
        return UserPrincipal.builder()
                .id(id)
                .build();
    }


    private static List<SimpleGrantedAuthority> convertSysPermissions(Set<Permission> list) {
        return list.stream()
                .map(x -> new SimpleGrantedAuthority(x.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
