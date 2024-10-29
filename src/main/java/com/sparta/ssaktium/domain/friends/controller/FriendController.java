package com.sparta.ssaktium.domain.friends.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.friends.dto.responseDto.FriendPageResponseDto;
import com.sparta.ssaktium.domain.friends.dto.responseDto.FriendResponseDto;
import com.sparta.ssaktium.domain.friends.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
@Tag(name = "친구 기능", description = "친구 관련 요청, 승인, 거절, 취소 등 기능")
public class FriendController {

    private final FriendService friendService;


    @PostMapping("/friends/{id}")
    @Operation(summary = "친구 요청", description = "친구 요청할 때 사용하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다", content = @Content(mediaType = "application/json"))
    @Parameters({
            @Parameter(name = "id", description = "요청할 친구 ID값", example = "1")
    })
    public ResponseEntity<CommonResponse<FriendResponseDto>> requestOrAcceptFriend(@AuthenticationPrincipal AuthUser authUser,
                                                                                   @PathVariable Long id) {
        FriendResponseDto responseDto = friendService.requestOrAcceptFriend(authUser.getUserId(), id);
        return ResponseEntity.ok(CommonResponse.success(responseDto));
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
    public ResponseEntity<CommonResponse<Page<FriendPageResponseDto>>> getFriends(@AuthenticationPrincipal AuthUser authUser,
                                                                                  @RequestParam(defaultValue = "1") int page,
                                                                                  @RequestParam(defaultValue = "10") int size) {
        Page<FriendPageResponseDto> responseDtos = friendService.getFriends(authUser.getUserId(), page, size);
        return ResponseEntity.ok(CommonResponse.success(responseDtos));
    }

    /**
     * 친구 요청 취소, 거절, 삭제 API
     *
     * @param authUser
     * @param id
     * @return
     */
    @DeleteMapping("/friends/{id}")
    public ResponseEntity<CommonResponse<String>> cancelOrDeleteFriend(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long id) {
        return ResponseEntity.ok(CommonResponse.success(friendService.cancelOrDeleteFriend(authUser.getUserId(), id)));
    }

}