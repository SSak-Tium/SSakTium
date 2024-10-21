package com.sparta.springusersetting.domain.user.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.user.dto.request.UserRoleChangeRequestDto;
import com.sparta.springusersetting.domain.user.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;

    // 유저 권한 변경
    @PatchMapping("/admin/users/{userId}")
    public ResponseEntity<ApiResponse<String>> changeUserRole(@PathVariable long userId, @RequestBody UserRoleChangeRequestDto userRoleChangeRequestDto) {
        return ResponseEntity.ok(ApiResponse.success(userAdminService.changeUserRole(userId, userRoleChangeRequestDto)));
    }
}
