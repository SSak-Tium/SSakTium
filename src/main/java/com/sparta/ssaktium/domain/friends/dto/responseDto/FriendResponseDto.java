package com.sparta.ssaktium.domain.friends.dto.responseDto;

import com.sparta.ssaktium.domain.friends.entity.FriendStatus;
import com.sparta.ssaktium.domain.friends.entity.Friends;
import com.sparta.ssaktium.domain.users.entity.Users;
import lombok.Getter;

@Getter
public class FriendResponseDto {

    private Long id;
    private Long myUserId;
    private Long friendUserId;
    private FriendStatus status;

    // Friends 엔티티에서 상태를 가져오는 생성자
    public FriendResponseDto(Friends friends, Users myUser, Users friendUser) {
        this.id = friends.getId();
        this.myUserId = myUser.getId();
        this.friendUserId = friendUser.getId();
        this.status = friends.getFriendStatus();
    }
}