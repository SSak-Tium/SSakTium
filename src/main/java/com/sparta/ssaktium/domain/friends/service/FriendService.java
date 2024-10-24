package com.sparta.ssaktium.domain.friends.service;

import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.friends.dto.responseDto.FriendPageResponseDto;
import com.sparta.ssaktium.domain.friends.dto.responseDto.FriendResponseDto;
import com.sparta.ssaktium.domain.friends.entity.FriendStatus;
import com.sparta.ssaktium.domain.friends.entity.Friend;
import com.sparta.ssaktium.domain.friends.exception.*;
import com.sparta.ssaktium.domain.friends.repository.FriendRepository;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.service.UserService;
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
            throw new SelfRequestException();
        }

        Optional<Friend> checkAlreadyRequest = friendRepository.findByUserIdAndFriendUserId(user.getId(), friendUser.getId());
        if (checkAlreadyRequest.isPresent()) {
            throw new FriendRequestAlreadySentException();
        }

        Friend friend = new Friend(user, friendUser);
        friendRepository.save(friend);

        return new FriendResponseDto(friend, user, friendUser);
    }

    public void cancelFriend(AuthUser authUser, Long id) {

        User user = User.fromAuthUser(authUser);
        userService.findUser(user.getId());

        Optional<Friend> friendRequest = friendRepository.findByUserIdAndFriendUserId(user.getId(), id);

        if (friendRequest.isEmpty()) {
            throw new NotFoundRequestFriendException();
        }

        Friend friend = friendRequest.get();

        if (!friend.getUserId().getId().equals(user.getId())) {
            throw new UnauthorizedFriendRequestCancellationException();
        }

        if (friend.getFriendStatus() != FriendStatus.PENDING) {
            throw new InvalidFriendRequestStatusException();
        }

        friendRepository.delete(friend);
    }

    public FriendResponseDto acceptFriend(AuthUser authUser, Long id) {

        User user = User.fromAuthUser(authUser);
        userService.findUser(user.getId());

        Optional<Friend> friendRequest = friendRepository.findByUserIdAndFriendUserId(id, user.getId());

        if (friendRequest.isEmpty()) {
            throw new NotFoundRequestFriendException();
        }

        Friend friend = friendRequest.get();

        if (!friend.getFriendUserId().getId().equals(user.getId())) {
            throw new UnauthorizedFriendRequestAcceptanceException();
        }

        if (friend.getFriendStatus() == FriendStatus.ACCEPTED) {
            throw new AlreadyAcceptedFriendException();
        }

        friend.acceptFriend();
        friendRepository.save(friend);

        return new FriendResponseDto(friend, friend.getFriendUserId(), friend.getUserId());
    }

    public FriendResponseDto rejectFriend(AuthUser authUser, Long id) {
        User user = User.fromAuthUser(authUser);
        userService.findUser(user.getId());

        Optional<Friend> friendRequest = friendRepository.findByUserIdAndFriendUserId(id, user.getId());

        if (friendRequest.isEmpty()) {
            throw new NotFoundRequestFriendException();
        }

        Friend friend = friendRequest.get();

        if (!friend.getFriendUserId().getId().equals(user.getId())) {
            throw new UnauthorizedFriendRequestAcceptanceException();
        }

        if (friend.getFriendStatus() != FriendStatus.PENDING) {
            throw new AlreadyAcceptedFriendException();
        }

        friend.rejectFriend();
        friendRepository.save(friend);

        return new FriendResponseDto(friend, friend.getFriendUserId(), friend.getUserId());
    }


    @Transactional(readOnly = true)
    public Page<FriendPageResponseDto> getFriends(AuthUser authUser, int page, int size) {

        User user = User.fromAuthUser(authUser);
        userService.findUser(user.getId());

        // ACCEPTED 상태의 친구만 조회
        Page<Friend> friendsPage =
                friendRepository.findByUserIdOrFriendIdAndStatus(
                        user.getId(),
                        user.getId(),
                        FriendStatus.ACCEPTED,
                        PageRequest.of(page - 1, size
                        )
        );

        return friendsPage.map(friend -> {
            Long friendId = friend.getFriendUserId().getId().equals(user.getId())
                    ? friend.getUserId().getId()
                    : friend.getFriendUserId().getId();
            return new FriendPageResponseDto(friend, friendId);
        });
    }

    public void deleteFriend(AuthUser authUser, Long id) {
        User user = User.fromAuthUser(authUser);
        userService.findUser(user.getId());

        // 친구 관계를 조회
        Optional<Friend> friendRelationship = friendRepository.findByUserIdAndFriendId(user.getId(), id);

        // 친구 관계가 없으면, 반대의 경우도 확인
        if (friendRelationship.isEmpty()) {
            friendRelationship = friendRepository.findByUserIdAndFriendId(id, user.getId());
        }

        // 친구 관계가 존재하지 않는 경우 예외 처리
        if (friendRelationship.isEmpty()) {
            throw new NotFoundFriendException();
        }

        // 친구 관계 삭제
        friendRepository.delete(friendRelationship.get());
    }
}
