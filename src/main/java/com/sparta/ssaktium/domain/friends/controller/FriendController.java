package com.sparta.ssaktium.domain.friends.controller;

import com.sparta.ssaktium.config.ApiResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.friends.dto.responseDto.FriendPageResponseDto;
import com.sparta.ssaktium.domain.friends.dto.responseDto.FriendResponseDto;
import com.sparta.ssaktium.domain.friends.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class FriendController {

    private final FriendService friendService;

    /**
     * 친구 요청 또는 수락API
     *
     * @param authUser
     * @param id
     * @return
     */
    @PostMapping("/friends/{id}")
    public ResponseEntity<ApiResponse<FriendResponseDto>> requestOrAcceptFriend(@AuthenticationPrincipal AuthUser authUser,
                                                                                @PathVariable Long id) {
        FriendResponseDto responseDto = friendService.requestOrAcceptFriend(authUser.getUserId(), id);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    /**
     * 친구 목록조회 API
     *
     * @param authUser
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/friends")
    public ResponseEntity<ApiResponse<Page<FriendPageResponseDto>>> getFriends(@AuthenticationPrincipal AuthUser authUser,
                                                                               @RequestParam(defaultValue = "1") int page,
                                                                               @RequestParam(defaultValue = "10") int size) {
        Page<FriendPageResponseDto> responseDtos = friendService.getFriends(authUser.getUserId(), page, size);
        return ResponseEntity.ok(ApiResponse.success(responseDtos));
    }

    /**
     * 친구 요청 취소, 거절, 삭제 API
     *
     * @param authUser
     * @param id
     * @return
     */
    @DeleteMapping("/friends/{id}")
    public ResponseEntity<ApiResponse<String>> cancelOrDeleteFriend(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(friendService.cancelOrDeleteFriend(authUser.getUserId(), id)));
    }

}