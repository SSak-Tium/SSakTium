package com.sparta.springusersetting.domain.friends.dto.responseDto;

import com.sparta.springusersetting.domain.friends.entity.FriendStatus;

import com.sparta.springusersetting.domain.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendRequestResponseDto {

    private Long id;
    private Long myUserId;
    private Long friendUserId;
    private FriendStatus status;

    public FriendRequestResponseDto(User user, User friendUser) {
        this.myUserId = user.getId();
        this.friendUserId = friendUser.getId();
        this.status = FriendStatus.PENDING;
    }

}