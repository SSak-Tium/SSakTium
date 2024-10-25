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
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private User friendUser;

    @Enumerated(EnumType.STRING)
    private FriendStatus friendStatus;

    public Friend(User user, User friendUser) {
        this.user = user;
        this.friendUser = friendUser;
        this.friendStatus = FriendStatus.PENDING;
    }

    public void acceptFriend() {
        this.friendStatus = FriendStatus.ACCEPTED;
    }
}