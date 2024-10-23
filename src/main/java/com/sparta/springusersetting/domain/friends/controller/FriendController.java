package com.sparta.springusersetting.domain.friends.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.friends.dto.responseDto.FriendPageResponseDto;
import com.sparta.springusersetting.domain.friends.dto.responseDto.FriendResponseDto;
import com.sparta.springusersetting.domain.friends.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    /**
     * 친구요청 API
     *
     * @param authUser
     * @param id
     * @return
     */
    @PostMapping("/v1/users/{id}/friends")
    public ResponseEntity<ApiResponse<FriendResponseDto>> requestFriend(@AuthenticationPrincipal AuthUser authUser,
                                                                        @PathVariable Long id) {
        FriendResponseDto responseDto = friendService.requestFriend(authUser, id);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    /**
     * 친구 요청 취소 API
     *
     * @param authUser
     * @param id
     * @return
     */
    @DeleteMapping("/v1/users/{id}/friends")
    public ResponseEntity<ApiResponse<String>> cancelFriend(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long id) {
        friendService.cancelFriend(authUser, id);
        return ResponseEntity.ok(ApiResponse.success("친구 요청이 취소되었습니다."));
    }

    /**
     * 친구 요청 수락 API
     *
     * @param authUser
     * @param id
     * @return
     */
    @PutMapping("v1/friends/{id}/accept")
    public ResponseEntity<ApiResponse<FriendResponseDto>> acceptFriend(@AuthenticationPrincipal AuthUser authUser,
                                                                       @PathVariable Long id) {
        FriendResponseDto responseDto = friendService.acceptFriend(authUser, id);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    /**
     * 친구 요청 거절 API
     * @param authUser
     * @param id
     * @return
     */
    @PutMapping("/v1/friends/{id}/reject")
    public ResponseEntity<ApiResponse<FriendResponseDto>> rejectFriend(@AuthenticationPrincipal AuthUser authUser,
                                                                       @PathVariable Long id) {
        FriendResponseDto responseDto = friendService.rejectFriend(authUser, id);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    /**
     * 친구 목록조회 API
     * @param authUser
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/v1/friends")
    public ResponseEntity<ApiResponse<Page<FriendPageResponseDto>>> getFriends(@AuthenticationPrincipal AuthUser authUser,
                                                                  @RequestParam(defaultValue = "1") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        Page<FriendPageResponseDto> responseDtos = friendService.getFriends(authUser, page, size);
        return ResponseEntity.ok(ApiResponse.success(responseDtos));
    }


}