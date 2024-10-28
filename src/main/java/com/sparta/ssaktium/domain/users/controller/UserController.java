package com.sparta.ssaktium.domain.users.controller;

import com.sparta.ssaktium.config.ApiResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.users.dto.request.UserChangePasswordRequestDto;
import com.sparta.ssaktium.domain.users.dto.request.UserChangeRequestDto;
import com.sparta.ssaktium.domain.users.dto.request.UserCheckPasswordRequestDto;
import com.sparta.ssaktium.domain.users.dto.response.UserResponseDto;
import com.sparta.ssaktium.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 프로필 조회 ( id )
     * @param userId 사용자 id
     * @return
     */
    @GetMapping("/v1/users/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUser(userId)));
    }

    /**
     * 유저 비밀번호 수정
     * @param authUser 로그인 유저
     * @param userChangePasswordRequestDto 비밀번호 확인 : 이전 비밀번호, 새 비밀번호
     * @return
     */
    @PostMapping("/v1/users/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserChangePasswordRequestDto userChangePasswordRequestDto) {
        return ResponseEntity.ok(ApiResponse.success(userService.changePassword(authUser.getUserId(), userChangePasswordRequestDto)));
    }

    // 수정해야함 //
    @PutMapping("/vi/users")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserChangeRequestDto userChangeRequestDto) {
        return ResponseEntity.ok(ApiResponse.success(userService.updateUser(authUser.getUserId(), userChangeRequestDto)));
    }

    /**
     * 유저 회원탈퇴
     * @param authUser 로그인 유저
     * @param userCheckPasswordRequestDto 비밀번호 확인 : Password
     * @return
     */
    @DeleteMapping("/v1/users")
    public ResponseEntity<ApiResponse<String>> deleteUser(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserCheckPasswordRequestDto userCheckPasswordRequestDto) {
        return ResponseEntity.ok(ApiResponse.success(userService.deleteUser(authUser.getUserId(), userCheckPasswordRequestDto)));
    }
}
