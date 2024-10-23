package com.sparta.springusersetting.domain.friends.service;

import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.friends.dto.responseDto.FriendRequestResponseDto;
import com.sparta.springusersetting.domain.friends.entity.FriendStatus;
import com.sparta.springusersetting.domain.friends.entity.Friends;
import com.sparta.springusersetting.domain.friends.repository.FriendRepository;
import com.sparta.springusersetting.domain.users.entity.User;
import com.sparta.springusersetting.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserService userService;
    private final FriendRepository friendRepository;

    public FriendRequestResponseDto requestFriend(AuthUser authUser, Long id) {

        User user = User.fromAuthUser(authUser);
        userService.findUser(user.getId());

        User friendUser = userService.findUser(id);

        if (user.getId().equals(id)) {
            throw new IllegalArgumentException("You cannot invite yourself");
        }

        Optional<Friends> checkAlreadyRequest = friendRepository.findByUserIdAndFriendUserId(user.getId(), friendUser.getId());
        if (checkAlreadyRequest.isPresent()) {
            throw new IllegalArgumentException("해당 친구는 이미 요청을 보냈습니다.");
        }

        Friends friends = new Friends(user, friendUser);
        friendRepository.save(friends);

        return new FriendRequestResponseDto(user, friendUser);
    }

    public void cancelFriend(AuthUser authUser, Long id) {

        User user = User.fromAuthUser(authUser);
        userService.findUser(user.getId());

        Optional<Friends> friendRequest = friendRepository.findByUserIdAndFriendUserId(user.getId(), id);

        if (friendRequest.isEmpty()) {
            throw new IllegalArgumentException("해당 친구 요청을 찾을 수 없습니다.");
        }

        Friends friends = friendRequest.get();
        if (friends.getFriendStatus() != FriendStatus.PENDING) {
            throw new IllegalArgumentException("이미 수락했거나 거절된 친구 요청입니다.");
        }

        friendRepository.delete(friends);
    }
}
