package com.sparta.springusersetting.domain.user.service;

import com.sparta.springusersetting.domain.user.dto.request.UserRoleChangeRequestDto;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.enums.UserRole;
import com.sparta.springusersetting.domain.user.exception.NotFoundUserException;
import com.sparta.springusersetting.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;

    @Transactional
    public String changeUserRole(long userId, UserRoleChangeRequestDto userRoleChangeRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        user.updateRole(UserRole.of(userRoleChangeRequestDto.getRole()));
        return "유저 권한이 정상적으로 변경되었습니다.";
    }
}
