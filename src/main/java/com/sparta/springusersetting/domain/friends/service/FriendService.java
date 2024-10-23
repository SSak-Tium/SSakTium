package com.sparta.springusersetting.domain.friends.service;

import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.friends.dto.responseDto.FriendPageResponseDto;
import com.sparta.springusersetting.domain.friends.dto.responseDto.FriendResponseDto;
import com.sparta.springusersetting.domain.friends.entity.FriendStatus;
import com.sparta.springusersetting.domain.friends.entity.Friends;
import com.sparta.springusersetting.domain.friends.repository.FriendRepository;
import com.sparta.springusersetting.domain.users.entity.User;
import com.sparta.springusersetting.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendService {

    private final UserService userService;
    private final FriendRepository friendRepository;

    public FriendResponseDto requestFriend(AuthUser authUser, Long id) {

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

        return new FriendResponseDto(friends, user, friendUser);
    }

    public void cancelFriend(AuthUser authUser, Long id) {

        User user = User.fromAuthUser(authUser);
        userService.findUser(user.getId());

        Optional<Friends> friendRequest = friendRepository.findByUserIdAndFriendUserId(user.getId(), id);

        if (friendRequest.isEmpty()) {
            throw new IllegalArgumentException("해당 친구 요청을 찾을 수 없습니다.");
        }

        Friends friends = friendRequest.get();

        if (!friends.getUserId().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인이 보낸 친구 요청만 취소할 수 있습니다.");
        }

        if (friends.getFriendStatus() != FriendStatus.PENDING) {
            throw new IllegalArgumentException("이미 수락했거나 거절된 친구 요청입니다.");
        }

        friendRepository.delete(friends);
    }

    public FriendResponseDto acceptFriend(AuthUser authUser, Long id) {

        User user = User.fromAuthUser(authUser);
        userService.findUser(user.getId());

        Optional<Friends> friendRequest = friendRepository.findByUserIdAndFriendUserId(id, user.getId());

        if (friendRequest.isEmpty()) {
            throw new IllegalArgumentException("해당 친구 요청을 찾을 수 없습니다.");
        }

        Friends friends = friendRequest.get();

        if (!friends.getFriendUserId().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인이 받은 친구 요청만 수락할 수 있습니다.");
        }

        if (friends.getFriendStatus() == FriendStatus.ACCEPTED) {
            throw new IllegalArgumentException("이미 수락한 친구입니다.");
        }

        friends.acceptFriend();
        friendRepository.save(friends);

        return new FriendResponseDto(friends, friends.getFriendUserId(), friends.getUserId());
    }

    public FriendResponseDto rejectFriend(AuthUser authUser, Long id) {
        User user = User.fromAuthUser(authUser);
        userService.findUser(user.getId());

        Optional<Friends> friendRequest = friendRepository.findByUserIdAndFriendUserId(id, user.getId());

        if (friendRequest.isEmpty()) {
            throw new IllegalArgumentException("해당 친구 요청을 찾을 수 없습니다.");
        }

        Friends friends = friendRequest.get();

        if (!friends.getFriendUserId().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인이 받은 친구 요청만 수락할 수 있습니다.");
        }

        if (friends.getFriendStatus() != FriendStatus.PENDING) {
            throw new IllegalArgumentException("이미 수락 또는 거절한 친구입니다.");
        }

        friends.rejectFriend();
        friendRepository.save(friends);

        return new FriendResponseDto(friends, friends.getFriendUserId(), friends.getUserId());
    }


    @Transactional(readOnly = true)
    public Page<FriendPageResponseDto> getFriends(AuthUser authUser, int page, int size) {

        User user = User.fromAuthUser(authUser);
        userService.findUser(user.getId());

        // ACCEPTED 상태의 친구만 조회
        Page<Friends> friendsPage =
                friendRepository.findByUserIdOrFriendIdAndStatus(
                        user.getId(),
                        user.getId(),
                        FriendStatus.ACCEPTED,
                        PageRequest.of(page - 1, size
                        )
        );

        return friendsPage.map(friends -> {
            Long friendId = friends.getFriendUserId().getId().equals(user.getId())
                    ? friends.getUserId().getId()
                    : friends.getFriendUserId().getId();
            return new FriendPageResponseDto(friends, friendId);
        });
    }
}
