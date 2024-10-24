package com.sparta.ssaktium.domain.friends.dto.responseDto;

import com.sparta.ssaktium.domain.friends.entity.FriendStatus;
import com.sparta.ssaktium.domain.friends.entity.Friend;
import com.sparta.ssaktium.domain.users.entity.User;
import lombok.Getter;

@Getter
public class FriendResponseDto {

    private Long id;
    private Long myUserId;
    private Long friendUserId;
    private FriendStatus status;

    // Friends 엔티티에서 상태를 가져오는 생성자
    public FriendResponseDto(Friend friend, User myUser, User friendUser) {
        this.id = friend.getId();
        this.myUserId = myUser.getId();
        this.friendUserId = friendUser.getId();
        this.status = friend.getFriendStatus();
    }
}