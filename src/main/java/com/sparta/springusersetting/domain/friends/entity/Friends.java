package com.sparta.springusersetting.domain.friends.entity;

import com.sparta.springusersetting.domain.common.entity.Timestamped;

import com.sparta.springusersetting.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Friends extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users userId;

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private Users friendUserId;

    private FriendStatus friendStatus;


    public Friends(Long id, Users userId, Users friendUserId, FriendStatus friendStatus) {
        this.id = id;
        this.userId = userId;
        this.friendUserId = friendUserId;
        this.friendStatus = friendStatus;
    }

    public Friends(Users userId, Users friendUserId) {
        this.userId = userId;
        this.friendUserId = friendUserId;
        this.friendStatus = FriendStatus.PENDING;
    }

    public void acceptFriend() {
        this.friendStatus = FriendStatus.ACCEPTED;
    }

    public void rejectFriend() {
        this.friendStatus = FriendStatus.REJECTED;
    }
}