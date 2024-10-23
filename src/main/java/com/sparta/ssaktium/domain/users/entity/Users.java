package com.sparta.ssaktium.domain.users.entity;

import com.sparta.ssaktium.domain.auth.dto.request.AdminSignupRequestDto;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.common.entity.Timestamped;
import com.sparta.ssaktium.domain.users.enums.UserRole;
import com.sparta.ssaktium.domain.users.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class Users extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    private String userName;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;


    public Users(String email, String password, String userName, LocalDate birthDate, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.birthDate = birthDate;
        this.userRole = userRole;
        this.userStatus = UserStatus.ACTIVE;
    }

    public Users(String email, String password, String userName, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.userRole = userRole;
        this.userStatus = UserStatus.ACTIVE;
    }

    private Users(Long id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }

    public static Users fromAuthUser(AuthUser authUser) {
        String roleName = authUser.getAuthorities().iterator().next().getAuthority();
        return new Users(authUser.getUserId(), authUser.getEmail(), UserRole.of(roleName));
    }

    public static Users addAdminUser(AdminSignupRequestDto adminSignupRequestDto, String encodedPassword, UserRole userRole) {
        return new Users(adminSignupRequestDto.getEmail(), encodedPassword, adminSignupRequestDto.getUserName(), userRole);
    }

    // 유저 비밀번호 변경
    public void changePassword(String password) {
        this.password = password;
    }

    // 유저 상태 삭제 처리
    public void delete() {
        this.userStatus = UserStatus.DELETED;
    }
}
