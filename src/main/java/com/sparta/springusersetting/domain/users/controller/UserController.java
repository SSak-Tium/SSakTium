package com.sparta.springusersetting.domain.users.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.users.dto.request.UserChangePasswordRequestDto;
import com.sparta.springusersetting.domain.users.dto.request.UserCheckPasswordRequestDto;
import com.sparta.springusersetting.domain.users.dto.response.UserResponseDto;
import com.sparta.springusersetting.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 유저 조회 ( id )
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUser(userId)));
    }

    // 유저 비밀번호 변경
    @PutMapping("/user")
    public ResponseEntity<ApiResponse<String>> changePassword(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserChangePasswordRequestDto userChangePasswordRequestDto) {
        return ResponseEntity.ok(ApiResponse.success(userService.changePassword(authUser.getUserId(), userChangePasswordRequestDto)));
    }

    // 유저 회원탈퇴
    @DeleteMapping("/user")
    public ResponseEntity<ApiResponse<String>> deleteUser(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserCheckPasswordRequestDto userCheckPasswordRequestDto) {
        return ResponseEntity.ok(ApiResponse.success(userService.deleteUser(authUser.getUserId(), userCheckPasswordRequestDto)));
    }
}
