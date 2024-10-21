package com.sparta.springusersetting.domain.user.entity;

import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.common.entity.Timestamped;
import com.sparta.springusersetting.domain.user.enums.UserRole;
import com.sparta.springusersetting.domain.user.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    private String userName;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;


    public User(String email, String password, String userName, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.userRole = userRole;
        this.userStatus = UserStatus.ACTIVE;
    }

    private User(Long id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }

    public static User fromAuthUser(AuthUser authUser) {
        String roleName = authUser.getAuthorities().iterator().next().getAuthority();
        return new User(authUser.getUserId(), authUser.getEmail(), UserRole.of(roleName));
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void delete() {
        this.userStatus = UserStatus.DELETED;
    }
}
