package com.sparta.ssaktium.domain.user.service;

import com.sparta.ssaktium.domain.auth.exception.InvalidPasswordFormatException;
import com.sparta.ssaktium.domain.auth.exception.UnauthorizedPasswordException;
import com.sparta.ssaktium.domain.user.dto.request.UserChangePasswordRequestDto;
import com.sparta.ssaktium.domain.user.dto.request.UserCheckPasswordRequestDto;
import com.sparta.ssaktium.domain.user.dto.response.UserResponseDto;
import com.sparta.ssaktium.domain.user.entity.User;
import com.sparta.ssaktium.domain.user.exception.DuplicatePasswordException;
import com.sparta.ssaktium.domain.user.exception.NotFoundUserException;
import com.sparta.ssaktium.domain.user.repository.UserRepository;
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
        User user = findUser(userId);
        return new UserResponseDto(user.getId(), user.getEmail());
    }

    // 유저 비밀번호 변경
    @Transactional
    public String changePassword(long userId, UserChangePasswordRequestDto userChangePasswordRequestDto) {
        validateNewPassword(userChangePasswordRequestDto);

        // 유저 조회
        User user = findUser(userId);

        // 새 비밀번호 확인
        if (passwordEncoder.matches(userChangePasswordRequestDto.getNewPassword(), user.getPassword())) {
            throw new DuplicatePasswordException();
        }

        // 이전 비밀번호 확인
        if (!passwordEncoder.matches(userChangePasswordRequestDto.getOldPassword(), user.getPassword())) {
            throw new UnauthorizedPasswordException();
        }

        user.changePassword(passwordEncoder.encode(userChangePasswordRequestDto.getNewPassword()));

        return "비밀번호가 정상적으로 변경되었습니다.";
    }

    // 유저 회원탈퇴
    @Transactional
    public String deleteUser(long userId, UserCheckPasswordRequestDto userCheckPasswordRequestDto) {
        // 유저 조회
        User user = findUser(userId);

        //비밀번호 확인
        if (!passwordEncoder.matches(userCheckPasswordRequestDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedPasswordException();
        }

        // UserStatus DELETED 로 수정
        user.delete();

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
    public User findUser(long userId) {
        return userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
    }
}
