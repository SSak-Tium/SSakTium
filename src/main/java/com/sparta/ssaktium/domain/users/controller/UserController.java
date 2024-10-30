package com.sparta.ssaktium.domain.users.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.users.dto.request.UserChangePasswordRequestDto;
import com.sparta.ssaktium.domain.users.dto.request.UserChangeRequestDto;
import com.sparta.ssaktium.domain.users.dto.request.UserCheckPasswordRequestDto;
import com.sparta.ssaktium.domain.users.dto.response.UserImageResponseDto;
import com.sparta.ssaktium.domain.users.dto.response.UserResponseDto;
import com.sparta.ssaktium.domain.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "유저 기능", description = "유저 관련 조회, 수정, 탈퇴 기능")
public class UserController {

    private final UserService userService;

    @GetMapping("/v1/users/{Id}")
    @Operation(summary = "프로필 조회", description = "프로필 조회할 때 사용하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다")
    @Parameters({
            @Parameter(name = "id", description = "요청할 유저 ID값", example = "1")
    })
    public ResponseEntity<CommonResponse<UserResponseDto>> getUser(
            @PathVariable long Id
    ) {
        return ResponseEntity.ok(CommonResponse.success(userService.getUser(Id)));
    }

    /**
     * 유저 비밀번호 수정
     *
     * @param authUser                     로그인 유저
     * @param userChangePasswordRequestDto 비밀번호 확인 : 이전 비밀번호, 새 비밀번호
     * @return
     */
    @PostMapping("/v1/users/change-password")
    public ResponseEntity<CommonResponse<String>> changePassword(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody UserChangePasswordRequestDto userChangePasswordRequestDto
    ) {
        return ResponseEntity.ok(CommonResponse.success(userService.changePassword(authUser.getUserId(), userChangePasswordRequestDto)));
    }

    /**
     * 유저 회원정보 수정
     *
     * @param authUser             로그인 유저
     * @param userChangeRequestDto 프로필 이미지, 이름
     * @return
     */
    @PutMapping("/v1/users/{id}")
    public ResponseEntity<CommonResponse<UserResponseDto>> updateUser(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable long id,
            @RequestBody UserChangeRequestDto userChangeRequestDto
    ) {
        return ResponseEntity.ok(CommonResponse.success(userService.updateUser(authUser.getUserId(), id, userChangeRequestDto)));
    }

    /**
     * 유저 이미지 수정
     *
     * @param image    프로필 이미지
     * @return
     */
    @PostMapping(value = "/v1/users/update-image")
    public ResponseEntity<CommonResponse<UserImageResponseDto>> updateDictionary(
            @RequestPart MultipartFile image
    ) {
        return ResponseEntity.ok(CommonResponse.success(userService.updateUserImage(image)));
    }

    /**
     * 유저 회원탈퇴
     *
     * @param authUser                    로그인 유저
     * @param userCheckPasswordRequestDto 비밀번호 확인 : Password
     * @param id                          변경 유저 id
     * @return
     */
    @DeleteMapping("/v1/users/{id}")
    public ResponseEntity<CommonResponse<String>> deleteUser(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable long id,
            @RequestBody UserCheckPasswordRequestDto userCheckPasswordRequestDto

    ) {
        return ResponseEntity.ok(CommonResponse.success(userService.deleteUser(authUser.getUserId(), id, userCheckPasswordRequestDto)));
    }
}
