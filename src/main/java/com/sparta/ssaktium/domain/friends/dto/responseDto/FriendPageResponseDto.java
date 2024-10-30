package com.sparta.ssaktium.domain.friends.dto.responseDto;

import com.sparta.ssaktium.domain.friends.entity.Friend;
import com.sparta.ssaktium.domain.friends.entity.FriendStatus;
import lombok.Getter;

@Getter
public class FriendPageResponseDto {

    private Long id;
    private Long friendId;
    private FriendStatus status;

    public FriendPageResponseDto(Friend friend, Long friendId) {
        this.id = friend.getId();
        this.friendId = friendId;
        this.status = friend.getFriendStatus();
    }
}
