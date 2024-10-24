package com.sparta.ssaktium.domain.friends.entity;

import com.sparta.ssaktium.domain.common.entity.Timestamped;

import com.sparta.ssaktium.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Friend extends Timestamped {

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


    public Friend(Long id, User userId, User friendUserId, FriendStatus friendStatus) {
        this.id = id;
        this.userId = userId;
        this.friendUserId = friendUserId;
        this.friendStatus = friendStatus;
    }

    public Friend(User userId, User friendUserId) {
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