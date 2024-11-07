package com.sparta.ssaktium.domain.friends.service;

import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.friends.dto.responseDto.FriendPageResponseDto;
import com.sparta.ssaktium.domain.friends.dto.responseDto.FriendResponseDto;
import com.sparta.ssaktium.domain.friends.entity.Friend;
import com.sparta.ssaktium.domain.friends.entity.FriendStatus;
import com.sparta.ssaktium.domain.friends.exception.*;
import com.sparta.ssaktium.domain.friends.repository.FriendRepository;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.enums.UserRole;
import com.sparta.ssaktium.domain.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FriendServiceTest {

    @InjectMocks
    private FriendService friendService;

    @Mock
    private UserService userService;

    @Mock
    private FriendRepository friendRepository;

    private AuthUser authUser;
    private AuthUser friendAuthUser;
    private Friend friend;
    private User user;
    private User friendUser;

    @BeforeEach
    void setUp() {
        authUser = new AuthUser(1L, "user1@test.com", UserRole.ROLE_USER);
        friendAuthUser = new AuthUser(2L, "user2@test.com", UserRole.ROLE_USER);
        user = User.fromAuthUser(authUser);
        friendUser = User.fromAuthUser(friendAuthUser);
        friend = new Friend(user, friendUser);
    }

    @Nested
    class 친구요청_테스트 {
//        @Test
//        void 친구요청_성공() {
//            // Given
//            given(userService.findUser(authUser.getUserId())).willReturn(user);
//            given(userService.findUser(friendAuthUser.getUserId())).willReturn(friendUser);
//            given(friendRepository.findFriendRelationshipBetweenUsers(authUser.getUserId(), friendAuthUser.getUserId()))
//                    .willReturn(Optional.empty());
//            given(friendRepository.save(any(Friend.class)))
//                    .willAnswer(invocation -> invocation.getArgument(0));
//
//            // When
//            FriendResponseDto response = friendService.requestFriend(authUser.getUserId(), friendAuthUser.getUserId());
//
//            // Then
//            assertThat(response).isNotNull();
//            verify(friendRepository).save(any(Friend.class));
//        }

        @Test
        void 자기자신에게_친구요청시_예외발생() {
            // When & Then
            assertThrows(SelfRequestException.class,
                    () -> friendService.requestFriend(authUser.getUserId(), authUser.getUserId()));
        }

        @Test
        void 이미존재하는_친구요청시_예외발생() {
            // Given
            given(userService.findUser(authUser.getUserId())).willReturn(user);
            given(userService.findUser(friendAuthUser.getUserId())).willReturn(friendUser);
            given(friendRepository.findFriendRelationshipBetweenUsers(authUser.getUserId(), friendAuthUser.getUserId()))
                    .willReturn(Optional.of(friend));

            // When & Then
            assertThrows(FriendRequestAlreadySentException.class,
                    () -> friendService.requestFriend(authUser.getUserId(), friendAuthUser.getUserId()));
        }
    }

    @Nested
    class 친구수락_테스트 {
//        @Test
//        void 친구수락_성공() {
//            // Given
//            given(userService.findUser(authUser.getUserId())).willReturn(user);
//            given(userService.findUser(friendAuthUser.getUserId())).willReturn(friendUser);
//            given(friendRepository.findFriendRelationshipBetweenUsers(authUser.getUserId(), friendAuthUser.getUserId()))
//                    .willReturn(Optional.of(friend));
//            given(friendRepository.save(any(Friend.class)))
//                    .willAnswer(invocation -> invocation.getArgument(0));
//
//            // When
//            FriendResponseDto response = friendService.acceptFriend(authUser.getUserId(), friendAuthUser.getUserId());
//
//            // Then
//            assertThat(response).isNotNull();
//            assertThat(friend.getFriendStatus()).isEqualTo(FriendStatus.ACCEPTED);
//            verify(friendRepository).save(friend);
//        }

        @Test
        void 이미수락된_친구관계_예외발생() {
            // Given
            friend.acceptFriend();
            given(userService.findUser(authUser.getUserId())).willReturn(user);
            given(userService.findUser(friendAuthUser.getUserId())).willReturn(friendUser);
            given(friendRepository.findFriendRelationshipBetweenUsers(authUser.getUserId(), friendAuthUser.getUserId()))
                    .willReturn(Optional.of(friend));

            // When & Then
            assertThrows(AlreadyFriendsException.class,
                    () -> friendService.acceptFriend(authUser.getUserId(), friendAuthUser.getUserId()));
        }
    }

    @Nested
    class 친구목록조회_테스트 {
        @Test
        void 친구목록_조회_성공() {
            // Given
            AuthUser friendAuthUser2 = new AuthUser(3L, "user3@test.com", UserRole.ROLE_USER);
            List<Friend> friends = Arrays.asList(
                    friend,
                    new Friend(user, User.fromAuthUser(friendAuthUser2))
            );
            Page<Friend> friendPage = new PageImpl<>(friends);

            given(userService.findUser(authUser.getUserId())).willReturn(user);
            given(friendRepository.findFriendsByUserWithStatusOrder(eq(authUser.getUserId()), any(PageRequest.class)))
                    .willReturn(friendPage);

            // When
            Page<FriendPageResponseDto> response = friendService.getFriends(authUser.getUserId(), 1, 10);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getContent()).hasSize(2);
        }
    }

    @Nested
    class 친구삭제_테스트 {
        @Test
        void 친구삭제_성공() {
            // Given
            given(userService.findUser(authUser.getUserId())).willReturn(user);
            given(friendRepository.findFriendRelationshipBetweenUsers(authUser.getUserId(), friendAuthUser.getUserId()))
                    .willReturn(Optional.of(friend));

            // When
            friendService.deleteFriend(authUser.getUserId(), friendAuthUser.getUserId());

            // Then
            verify(friendRepository).delete(friend);
        }

        @Test
        void 존재하지않는_친구관계_삭제시_예외발생() {
            // Given
            given(userService.findUser(authUser.getUserId())).willReturn(user);
            given(friendRepository.findFriendRelationshipBetweenUsers(authUser.getUserId(), friendAuthUser.getUserId()))
                    .willReturn(Optional.empty());

            // When & Then
            assertThrows(NotFoundFriendException.class,
                    () -> friendService.deleteFriend(authUser.getUserId(), friendAuthUser.getUserId()));
        }
    }

    @Nested
    class 친구찾기_테스트 {
        @Test
        void 수락된_친구목록_조회_성공() {
            // Given
            AuthUser friendAuthUser2 = new AuthUser(3L, "user3@test.com", UserRole.ROLE_USER);
            User friendUser2 = User.fromAuthUser(friendAuthUser2);
            List<User> friends = Arrays.asList(friendUser, friendUser2);

            given(friendRepository.findFriendsByUser(authUser.getUserId(), FriendStatus.ACCEPTED))
                    .willReturn(friends);

            // When
            List<User> result = friendService.findFriends(authUser.getUserId());

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
            verify(friendRepository).findFriendsByUser(authUser.getUserId(), FriendStatus.ACCEPTED);
        }
    }
}