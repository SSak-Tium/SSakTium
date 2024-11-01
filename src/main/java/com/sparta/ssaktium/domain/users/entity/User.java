package com.sparta.ssaktium.domain.users.entity;

import com.sparta.ssaktium.domain.auth.dto.request.AdminSignupRequestDto;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.common.entity.Timestamped;
import com.sparta.ssaktium.domain.users.enums.UserRole;
import com.sparta.ssaktium.domain.users.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.hibernate.usertype.UserType;

import java.time.LocalDate;

@Getter
@Entity
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
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
    private String birthYear;
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private boolean deleted = Boolean.FALSE;

    public User(String email, String password, String userName, String birthYear, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.birthYear = birthYear;
        this.userRole = userRole;
    }

    public User(String email, String password, String userName, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.userRole = userRole;
    }

    // AuthUser -> User
    private User(Long id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }

    public User(Long id, String email, String password, String userName, String birthYear, String profileImageUrl, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.birthYear = birthYear;
        this.userRole = userRole;
    }

    public static User fromAuthUser(AuthUser authUser) {
        String roleName = authUser.getAuthorities().iterator().next().getAuthority();
        return new User(authUser.getUserId(), authUser.getEmail(), UserRole.of(roleName));
    }

    public static User addAdminUser(AdminSignupRequestDto adminSignupRequestDto, String encodedPassword, UserRole userRole) {
        return new User(adminSignupRequestDto.getEmail(), encodedPassword, adminSignupRequestDto.getUserName(), null, userRole);
    }

    // 유저 비밀번호 변경
    public void changePassword(String password) {
        this.password = password;
    }

    // 유저 정보 변경
    public void updateUser(String profileImageUrl, String userName) {
        this.profileImageUrl = profileImageUrl;
        this.userName = userName;
    }

    @Builder
    public static User createUser(Long id, String email, String password, String userName, String birthYear, String profileImageUrl, UserRole userRole) {
        return new User(id, email, password, userName, birthYear, profileImageUrl, userRole);
    }

    // 소셜 로그인 메서드
    public void socialLogin(String email, String userName) {
        this.email = email;
        this.userName= userName;
        this.userRole = UserRole.ROLE_USER;
    }
}
