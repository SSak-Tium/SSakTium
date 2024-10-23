package com.sparta.ssaktium.domain.friends.dto.responseDto;

import com.sparta.ssaktium.domain.friends.entity.Friends;
import lombok.Getter;

@Getter
public class FriendPageResponseDto {

    private Long id;
    private Long friendId;

    public FriendPageResponseDto(Friends friends, Long friendId) {
        this.id = friends.getId();
        this.friendId = friendId;
    }
}
