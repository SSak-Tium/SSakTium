package com.sparta.ssaktium.domain.users.dto.response;

import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class UserResponseDto {

    private final Long id;
    private final String email;
    private final String userName;
    private final LocalDate createdLocalDate;
    private final String birthYear;
    private final String profileImageUrl;
    private final UserRole userRole;
    private final List<Long> favoriteDictionaryList;

    public UserResponseDto(User user, List<Long> favoriteDictionaryList) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.createdLocalDate = user.getCreatedAt().toLocalDate();
        this.birthYear = user.getBirthYear();
        this.profileImageUrl = user.getProfileImageUrl();
        this.userRole = user.getUserRole();
        this.favoriteDictionaryList = favoriteDictionaryList;
    }
}
