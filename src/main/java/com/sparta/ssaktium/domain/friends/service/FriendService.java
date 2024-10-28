package com.sparta.ssaktium.domain.friends.service;

import com.sparta.ssaktium.domain.friends.dto.responseDto.FriendPageResponseDto;
import com.sparta.ssaktium.domain.friends.dto.responseDto.FriendResponseDto;
import com.sparta.ssaktium.domain.friends.entity.Friend;
import com.sparta.ssaktium.domain.friends.entity.FriendStatus;
import com.sparta.ssaktium.domain.friends.exception.*;
import com.sparta.ssaktium.domain.friends.repository.FriendRepository;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendService {

    private final UserService userService;
    private final FriendRepository friendRepository;

    @Transactional
    public FriendResponseDto requestOrAcceptFriend(Long userId, Long friendId) {

        if (Objects.equals(userId, friendId)) {
            throw new SelfRequestException();
        }

        User user = userService.findUser(userId);
        User friendUser = userService.findUser(friendId);

        Optional<Friend> friendRelationship = findFriendRelationship(user.getId(), friendUser.getId());

        if (friendRelationship.isEmpty()) {
            Friend newFriendRequest = new Friend(user, friendUser);
            friendRepository.save(newFriendRequest);
            return new FriendResponseDto(newFriendRequest, user, friendUser);
        }

        Friend existingFriendRequest = friendRelationship.get();

        if (existingFriendRequest.getFriendStatus() == FriendStatus.ACCEPTED) {
            throw new AlreadyFriendsException();
        }
        else if (existingFriendRequest.getFriendStatus() == FriendStatus.PENDING) {
            if (existingFriendRequest.getUser().getId().equals(userId)) {
                throw new FriendRequestAlreadySentException();
            } else {
                existingFriendRequest.acceptFriend();
                friendRepository.save(existingFriendRequest);
                return new FriendResponseDto(existingFriendRequest, existingFriendRequest.getFriendUser(), existingFriendRequest.getUser());
            }
        }

        existingFriendRequest.acceptFriend();
        friendRepository.save(existingFriendRequest);
        return new FriendResponseDto(existingFriendRequest, existingFriendRequest.getFriendUser(), existingFriendRequest.getUser());
    }

    public Page<FriendPageResponseDto> getFriends(Long userId, int page, int size) {

        User user = userService.findUser(userId);

        Page<Friend> friendsPage =
                friendRepository.findByUserOrFriend(
                        user.getId(),
                        user.getId(),
                        PageRequest.of(page - 1, size
                        )
                );

        return friendsPage.map(friend -> {
            Long friendId = friend.getFriendUser().getId().equals(user.getId())
                    ? friend.getUser().getId()
                    : friend.getFriendUser().getId();
            return new FriendPageResponseDto(friend, friendId);
        });
    }

    @Transactional
    public String cancelOrDeleteFriend(Long userId, Long id) {

        User user = userService.findUser(userId);

        Optional<Friend> friendRelationship = friendRepository.findByUserAndFriend(user.getId(), id);

        if (friendRelationship.isEmpty()) {
            friendRelationship = friendRepository.findByUserAndFriend(id, user.getId());
        }

        if (friendRelationship.isEmpty()) {
            throw new NotFoundFriendException();
        }

        friendRepository.delete(friendRelationship.get());

        return "친구 거절/삭제 요청 완료";
    }

    private Optional<Friend> findFriendRelationship(Long userId, Long friendId) {
        Optional<Friend> friendRelationship = friendRepository.findByUserAndFriend(userId, friendId);
        if (friendRelationship.isEmpty()) {
            friendRelationship = friendRepository.findByUserAndFriend(friendId, userId);
        }
        return friendRelationship;
    }


    public List<User> findFriends(long userId) {
        return friendRepository.findFriendsByUser(userId, FriendStatus.ACCEPTED);
    }

}
