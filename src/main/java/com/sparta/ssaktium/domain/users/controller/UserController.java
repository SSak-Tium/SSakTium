package com.sparta.ssaktium.domain.users.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.users.dto.request.UserChangePasswordRequestDto;
import com.sparta.ssaktium.domain.users.dto.request.UserChangeRequestDto;
import com.sparta.ssaktium.domain.users.dto.request.UserCheckPasswordRequestDto;
import com.sparta.ssaktium.domain.users.dto.response.UserImageResponseDto;
import com.sparta.ssaktium.domain.users.dto.response.UserResponseDto;
import com.sparta.ssaktium.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 프로필 조회 ( id )
     *
     * @param userId 사용자 id
     * @return
     */
    @GetMapping("/v1/users/{userId}")
    public ResponseEntity<CommonResponse<UserResponseDto>> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(CommonResponse.success(userService.getUser(userId)));
    }

    /**
     * 유저 비밀번호 수정
     *
     * @param authUser                     로그인 유저
     * @param userChangePasswordRequestDto 비밀번호 확인 : 이전 비밀번호, 새 비밀번호
     * @return
     */
    @PostMapping("/v1/users/change-password")
    public ResponseEntity<CommonResponse<String>> changePassword(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserChangePasswordRequestDto userChangePasswordRequestDto) {
        return ResponseEntity.ok(CommonResponse.success(userService.changePassword(authUser.getUserId(), userChangePasswordRequestDto)));
    }

    /**
     * 유저 회원정보 수정
     *
     * @param authUser             로그인 유저
     * @param userChangeRequestDto 프로필 이미지, 이름
     * @return
     */
    @PutMapping("/v1/users")
    public ResponseEntity<CommonResponse<UserResponseDto>> updateUser(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody UserChangeRequestDto userChangeRequestDto
    ) {
        return ResponseEntity.ok(CommonResponse.success(userService.updateUser(authUser.getUserId(), userChangeRequestDto)));
    }

    /**
     * 유저 이미지 수정
     *
     * @param authUser 로그인 유저
     * @param image    프로필 이미지
     * @return
     */
    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/v1/users/image")
    public ResponseEntity<CommonResponse<UserImageResponseDto>> updateDictionary(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestPart MultipartFile image
    ) {
        return ResponseEntity.ok(CommonResponse.success(userService.updateUserImage(authUser.getUserId(), image)));
    }

    /**
     * 유저 회원탈퇴
     *
     * @param authUser                    로그인 유저
     * @param userCheckPasswordRequestDto 비밀번호 확인 : Password
     * @return
     */
    @DeleteMapping("/v1/users")
    public ResponseEntity<CommonResponse<String>> deleteUser(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserCheckPasswordRequestDto userCheckPasswordRequestDto) {
        return ResponseEntity.ok(CommonResponse.success(userService.deleteUser(authUser.getUserId(), userCheckPasswordRequestDto)));
    }
}
