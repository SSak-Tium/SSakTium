package com.sparta.ssaktium.domain.common.dto;

import com.sparta.ssaktium.domain.users.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
public class AuthUser {

    private final long userId;
    private final String email;
    private final String userName;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(long userId, String email, String userName, UserRole role) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.authorities = List.of(new SimpleGrantedAuthority(role.name()));
    }
}