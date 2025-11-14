package com.company.myappapi.security;

import com.company.myappapi.entity.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserUtil {
    private UserUtil() {
    }

    public static UserPrincipal getCurrentUser() {
        return ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    public static List<String> getUserRoles() {
        return getCurrentUser().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public static void setCurrentUser(Long userId) {
        var user = UserPrincipal.create(userId);
        var upt = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(upt);
    }


    public static User getUser() {
        return User.of(getCurrentUser().getId());
    }
}
