package com.sparta.springusersetting.domain.users.service;

import com.sparta.springusersetting.domain.auth.exception.InvalidPasswordFormatException;
import com.sparta.springusersetting.domain.auth.exception.UnauthorizedPasswordException;
import com.sparta.springusersetting.domain.users.dto.request.UserChangePasswordRequestDto;
import com.sparta.springusersetting.domain.users.dto.request.UserCheckPasswordRequestDto;
import com.sparta.springusersetting.domain.users.dto.response.UserResponseDto;
import com.sparta.springusersetting.domain.users.entity.Users;
import com.sparta.springusersetting.domain.users.exception.DuplicatePasswordException;
import com.sparta.springusersetting.domain.users.exception.NotFoundUserException;
import com.sparta.springusersetting.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 유저 조회 ( id )
    public UserResponseDto getUser(long userId) {
        // 유저 조회
        Users users = findUser(userId);
        return new UserResponseDto(users.getId(), users.getEmail());
    }

    // 유저 비밀번호 변경
    @Transactional
    public String changePassword(long userId, UserChangePasswordRequestDto userChangePasswordRequestDto) {
        validateNewPassword(userChangePasswordRequestDto);

        // 유저 조회
        Users users = findUser(userId);

        // 새 비밀번호 확인
        if (passwordEncoder.matches(userChangePasswordRequestDto.getNewPassword(), users.getPassword())) {
            throw new DuplicatePasswordException();
        }

        // 이전 비밀번호 확인
        if (!passwordEncoder.matches(userChangePasswordRequestDto.getOldPassword(), users.getPassword())) {
            throw new UnauthorizedPasswordException();
        }

        users.changePassword(passwordEncoder.encode(userChangePasswordRequestDto.getNewPassword()));

        return "비밀번호가 정상적으로 변경되었습니다.";
    }

    // 유저 회원탈퇴
    @Transactional
    public String deleteUser(long userId, UserCheckPasswordRequestDto userCheckPasswordRequestDto) {
        // 유저 조회
        Users users = findUser(userId);

        //비밀번호 확인
        if (!passwordEncoder.matches(userCheckPasswordRequestDto.getPassword(), users.getPassword())) {
            throw new UnauthorizedPasswordException();
        }

        // UserStatus DELETED 로 수정
        users.delete();

        return "회원탈퇴가 정상적으로 완료되었습니다.";
    }


    // 패스워드 조건 확인 메서드
    private static void validateNewPassword(UserChangePasswordRequestDto userChangePasswordRequestDto) {
        if (userChangePasswordRequestDto.getNewPassword().length() < 8 ||
                !userChangePasswordRequestDto.getNewPassword().matches(".*\\d.*") ||
                !userChangePasswordRequestDto.getNewPassword().matches(".*[A-Z].*")) {
            throw new InvalidPasswordFormatException();
        }
    }

    // 유저 조회 메서드
    public Users findUser(long userId) {
        return userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
    }
}
