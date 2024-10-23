package com.sparta.springusersetting.domain.friends.entity;

import com.sparta.springusersetting.domain.common.entity.Timestamped;

import com.sparta.springusersetting.domain.users.entity.User;
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
    private User userId;

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private User friendUserId;

    private FriendStatus friendStatus;


    public Friends(Long id, User userId, User friendUserId, FriendStatus friendStatus) {
        this.id = id;
        this.userId = userId;
        this.friendUserId = friendUserId;
        this.friendStatus = friendStatus;
    }

    public Friends(User userId, User friendUserId) {
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