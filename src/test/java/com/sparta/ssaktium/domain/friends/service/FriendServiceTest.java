package com.sparta.ssaktium.domain.friends.service;

import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.friends.dto.responseDto.FriendPageResponseDto;
import com.sparta.ssaktium.domain.friends.dto.responseDto.FriendResponseDto;
import com.sparta.ssaktium.domain.friends.entity.Friend;
import com.sparta.ssaktium.domain.friends.entity.FriendStatus;
import com.sparta.ssaktium.domain.friends.exception.FriendRequestAlreadySentException;
import com.sparta.ssaktium.domain.friends.exception.NotFoundFriendException;
import com.sparta.ssaktium.domain.friends.exception.NotFoundRequestFriendException;
import com.sparta.ssaktium.domain.friends.exception.SelfRequestException;
import com.sparta.ssaktium.domain.friends.repository.FriendRepository;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.enums.UserRole;
import com.sparta.ssaktium.domain.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FriendServiceTest {

    @Mock
    private FriendRepository friendRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private FriendService friendService;


    private AuthUser authUser;
    private AuthUser friendAuthUser;

    private User user;
    private User friendUser;

    @BeforeEach
    void setUp() {
        authUser = new AuthUser(1L, "11@11.com", UserRole.ROLE_USER);
        friendAuthUser = new AuthUser(2L, "22@22.com", UserRole.ROLE_USER);

        // User 객체 생성
        user = User.fromAuthUser(authUser);
        friendUser = User.fromAuthUser(friendAuthUser);
    }

    @Test
    void 친구_요청_성공() {
        // given
        when(userService.findUser(authUser.getUserId())).thenReturn(user);
        when(userService.findUser(friendAuthUser.getUserId())).thenReturn(friendUser);
        when(friendRepository.findByUserIdAndFriendUserId(user.getId(), friendUser.getId())).thenReturn(Optional.empty());

        // when
        FriendResponseDto responseDto = friendService.requestFriend(authUser, friendAuthUser.getUserId());

        // then
        assertNotNull(responseDto);
        assertEquals(responseDto.getMyUserId(), authUser.getUserId());
        assertEquals(responseDto.getFriendUserId(), friendAuthUser.getUserId());
        assertEquals(responseDto.getStatus(), FriendStatus.PENDING);

        // verify that the friend was saved
        verify(friendRepository).save(any(Friend.class));
    }

    @Test
    void 이미_보낸_친구_요청_예외() {
        // given
        when(userService.findUser(authUser.getUserId())).thenReturn(user);
        when(userService.findUser(friendAuthUser.getUserId())).thenReturn(friendUser);
        when(friendRepository.findByUserIdAndFriendUserId(user.getId(), friendUser.getId()))
                .thenReturn(Optional.of(new Friend(user, friendUser)));

        // when & then
        assertThrows(FriendRequestAlreadySentException.class, () -> {
            friendService.requestFriend(authUser, friendAuthUser.getUserId());
        });

        verify(friendRepository, never()).save(any(Friend.class));
    }

    @Test
    void 자기자신에게_친구_요청_예외() {
        // given
        when(userService.findUser(authUser.getUserId())).thenReturn(user);

        // when & then
        assertThrows(SelfRequestException.class, () -> {
            friendService.requestFriend(authUser, authUser.getUserId());
        });

        verify(friendRepository, never()).save(any(Friend.class));
    }

    @Test
    void 친구_요청_취소_성공() {
        // given
        Long friendUserId = 2L;
        User friendUser = User.fromAuthUser(friendAuthUser);
        Friend friend = new Friend(user, friendUser);

        when(userService.findUser(authUser.getUserId())).thenReturn(user);
        when(friendRepository.findByUserIdAndFriendUserId(user.getId(), friendUserId)).thenReturn(Optional.of(friend));

        // when
        friendService.cancelFriend(authUser, friendUserId);

        // then
        verify(friendRepository).delete(friend);
    }

    @Test
    void 친구_요청_취소_실패_친구_요청_없음() {
        // given
        Long friendUserId = 2L;

        when(userService.findUser(authUser.getUserId())).thenReturn(user);
        when(friendRepository.findByUserIdAndFriendUserId(user.getId(), friendUserId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundRequestFriendException.class, () -> friendService.cancelFriend(authUser, friendUserId));
        verify(friendRepository, never()).delete(any(Friend.class));
    }

    @Test
    void 친구_요청_수락_성공() {
        // given
        Long friendUserId = 2L;
        User friendUser = User.fromAuthUser(friendAuthUser);
        Friend friend = new Friend(friendUser, user);

        when(userService.findUser(authUser.getUserId())).thenReturn(user);
        when(friendRepository.findByUserIdAndFriendUserId(friendUserId, user.getId())).thenReturn(Optional.of(friend));

        // when
        FriendResponseDto responseDto = friendService.acceptFriend(authUser, friendUserId);

        // then
        assertNotNull(responseDto);
        assertEquals(FriendStatus.ACCEPTED, friend.getFriendStatus());
        verify(friendRepository).save(friend);
    }

    @Test
    void 친구_요청_수락_실패_요청_없음() {
        // given
        Long friendUserId = 2L;

        when(userService.findUser(authUser.getUserId())).thenReturn(user);
        when(friendRepository.findByUserIdAndFriendUserId(friendUserId, user.getId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundRequestFriendException.class, () -> friendService.acceptFriend(authUser, friendUserId));
        verify(friendRepository, never()).save(any(Friend.class));
    }

    @Test
    void 친구_요청_거절_성공() {
        // given
        Long friendUserId = 2L;
        User friendUser = User.fromAuthUser(friendAuthUser);
        Friend friend = new Friend(friendUser, user);

        when(userService.findUser(authUser.getUserId())).thenReturn(user);
        when(friendRepository.findByUserIdAndFriendUserId(friendUserId, user.getId())).thenReturn(Optional.of(friend));

        // when
        FriendResponseDto responseDto = friendService.rejectFriend(authUser, friendUserId);

        // then
        assertNotNull(responseDto);
        assertEquals(FriendStatus.REJECTED, friend.getFriendStatus());
        verify(friendRepository).save(friend);
    }

    @Test
    void 친구_요청_거절_실패_요청_없음() {
        // given
        Long friendUserId = 2L;

        when(userService.findUser(authUser.getUserId())).thenReturn(user);
        when(friendRepository.findByUserIdAndFriendUserId(friendUserId, user.getId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundRequestFriendException.class, () -> friendService.rejectFriend(authUser, friendUserId));
        verify(friendRepository, never()).save(any(Friend.class));
    }

    @Test
    void 친구_목록_조회_성공() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Friend friend = new Friend(user, friendUser);
        Page<Friend> friendsPage = new PageImpl<>(List.of(friend));

        when(userService.findUser(authUser.getUserId())).thenReturn(user);
        when(friendRepository.findByUserIdOrFriendIdAndStatus(user.getId(), user.getId(), FriendStatus.ACCEPTED, pageRequest))
                .thenReturn(friendsPage);

        // when
        Page<FriendPageResponseDto> result = friendService.getFriends(authUser, 1, 10);

        // then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void 친구_목록_조회_친구_없음() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Friend> friendsPage = Page.empty();

        when(userService.findUser(authUser.getUserId())).thenReturn(user);
        when(friendRepository.findByUserIdOrFriendIdAndStatus(user.getId(), user.getId(), FriendStatus.ACCEPTED, pageRequest))
                .thenReturn(friendsPage);

        // when
        Page<FriendPageResponseDto> result = friendService.getFriends(authUser, 1, 10);

        // then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void 친구_삭제_성공() {
        // given
        Long friendUserId = 2L;
        User friendUser = User.fromAuthUser(friendAuthUser);
        Friend friend = new Friend(user, friendUser);

        when(userService.findUser(authUser.getUserId())).thenReturn(user);
        when(friendRepository.findByUserIdAndFriendId(user.getId(), friendUserId)).thenReturn(Optional.of(friend));

        // when
        friendService.deleteFriend(authUser, friendUserId);

        // then
        verify(friendRepository).delete(friend);
    }

    @Test
    void 친구_삭제_실패_친구_없음() {
        // given
        Long friendUserId = 2L;

        when(userService.findUser(authUser.getUserId())).thenReturn(user);
        when(friendRepository.findByUserIdAndFriendId(user.getId(), friendUserId)).thenReturn(Optional.empty());
        when(friendRepository.findByUserIdAndFriendId(friendUserId, user.getId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundFriendException.class, () -> friendService.deleteFriend(authUser, friendUserId));
        verify(friendRepository, never()).delete(any(Friend.class));
    }

}