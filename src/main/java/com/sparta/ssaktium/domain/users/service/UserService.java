package com.sparta.ssaktium.domain.users.service;

import com.sparta.ssaktium.domain.auth.exception.UnauthorizedPasswordException;
import com.sparta.ssaktium.domain.common.exception.ForbiddenException;
import com.sparta.ssaktium.domain.common.service.S3Service;
import com.sparta.ssaktium.domain.dictionaries.dto.response.DictionaryImageResponseDto;
import com.sparta.ssaktium.domain.users.dto.request.UserChangePasswordRequestDto;
import com.sparta.ssaktium.domain.users.dto.request.UserChangeRequestDto;
import com.sparta.ssaktium.domain.users.dto.request.UserCheckPasswordRequestDto;
import com.sparta.ssaktium.domain.users.dto.response.UserImageResponseDto;
import com.sparta.ssaktium.domain.users.dto.response.UserResponseDto;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.enums.UserStatus;
import com.sparta.ssaktium.domain.users.exception.DuplicatePasswordException;
import com.sparta.ssaktium.domain.users.exception.NotFoundUserException;
import com.sparta.ssaktium.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    // 유저 조회 ( id )
    public UserResponseDto getUser(long userId) {
        // 유저 조회
        User user = findUser(userId);
        return new UserResponseDto(user);
    }

    // 유저 비밀번호 변경
    @Transactional
    public String changePassword(long userId, UserChangePasswordRequestDto userChangePasswordRequestDto) {
        // 유저 조회
        User user = findUser(userId);

        // 이전 비밀번호 확인
        if (!passwordEncoder.matches(userChangePasswordRequestDto.getOldPassword(), user.getPassword())) {
            throw new UnauthorizedPasswordException();
        }

        // 새 비밀번호 확인
        if (passwordEncoder.matches(userChangePasswordRequestDto.getNewPassword(), user.getPassword())) {
            throw new DuplicatePasswordException();
        }

        user.changePassword(passwordEncoder.encode(userChangePasswordRequestDto.getNewPassword()));

        return "비밀번호가 정상적으로 변경되었습니다.";
    }

    // 유저 회원정보 수정
    public UserResponseDto updateUser(long userId, UserChangeRequestDto userChangeRequestDto) {
        // 유저 조회
        User user = findUser(userId);

        // 유저 수정
        user.updateUser(userChangeRequestDto.getProfileImageUrl(), userChangeRequestDto.getUserName());

        // DB 저장
        userRepository.save(user);

        // DTO 반환
        return new UserResponseDto(user);
    }

    // 유저 프로필 사진 변경
    @Transactional
    public UserImageResponseDto updateUserImage(long userId, MultipartFile image) {
        // 유저 조회
        findUser(userId);

        // 업로드한 파일의 S3 URL 주소
        String imageUrl = s3Service.uploadImageToS3(image, s3Service.bucket);

        // DTO 반환
        return new UserImageResponseDto(imageUrl);
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

    // 유저 조회 메서드 ( id )
    public User findUser(long userId) {
        return userRepository.findByIdAndUserStatus(userId, UserStatus.ACTIVE).orElseThrow(NotFoundUserException::new);
    }
}
