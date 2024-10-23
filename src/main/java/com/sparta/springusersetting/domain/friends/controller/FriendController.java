package com.sparta.springusersetting.domain.friends.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.friends.dto.responseDto.FriendRequestResponseDto;
import com.sparta.springusersetting.domain.friends.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    /**
     * 친구 신청 API
     * @param authUser
     * @param id
     * @return "친구 요청 완료"
     */
    @PostMapping("/v1/users/{id}/friends")
    public ResponseEntity<ApiResponse<FriendRequestResponseDto>> requestFriend(@AuthenticationPrincipal AuthUser authUser,
                                                                               @PathVariable Long id) {
        FriendRequestResponseDto responseDto = friendService.requestFriend(authUser, id);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }


}