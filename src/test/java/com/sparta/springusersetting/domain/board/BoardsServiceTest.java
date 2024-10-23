package com.sparta.springusersetting.domain.board;

import com.sparta.springusersetting.domain.boards.dto.requestDto.BoardsSaveRequestDto;
import com.sparta.springusersetting.domain.boards.dto.responseDto.BoardsSaveResponseDto;
import com.sparta.springusersetting.domain.boards.entity.Boards;
import com.sparta.springusersetting.domain.boards.entity.PublicStatus;
import com.sparta.springusersetting.domain.boards.repository.BoardsRepository;
import com.sparta.springusersetting.domain.boards.service.BoardsService;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.enums.UserRole;
import com.sparta.springusersetting.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BoardsServiceTest {

    @Mock
    private BoardsRepository boardsRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private BoardsService boardsService;

    @Test
    public void 보드_생성_성공(){
        //given
        AuthUser authUser = new AuthUser(1L,"aa@aa.com", UserRole.ROLE_USER);
        BoardsSaveRequestDto requestDto = new BoardsSaveRequestDto("aa","aaa","aaaa", PublicStatus.ALL);
        User user = new User("aa@aa.com","aa","bb",UserRole.ROLE_USER);
        Boards boards = new Boards(requestDto);

        //when
        boardsService.saveBoards(authUser,requestDto);
        //then
        assertNotNull(boards);
        assertEquals(boards.getTitle(), requestDto.getTitle());
        assertEquals(boards.getContent(), requestDto.getContents());
        assertEquals(boards.getImage(), requestDto.getImages());
        assertEquals(boards.getPublicStatus(),requestDto.getPublicStatus());
    }

    @Test
    public void 보드_수정_성공() throws IllegalAccessException, NoSuchFieldException {
        //given
        AuthUser authUser = new AuthUser(1L,"aa@aa.com", UserRole.ROLE_USER);
        long boardId = 1L;
        BoardsSaveRequestDto requestDto = new BoardsSaveRequestDto("aa","aaa","aaaa", PublicStatus.ALL);
        // 게시글 소유자 생성
        User ownerUser = new User("aa@aa.com", "password", "name", UserRole.ROLE_USER);
//        BoardsSaveRequestDto updateRequestDto = new BoardsSaveRequestDto("initialTitle", "initialContents", "initialImages", PublicStatus.ALL, ownerUser);
//        Boards updateBoards = new Boards("initialTitle", "initialContents", "initialImages", PublicStatus.ALL, ownerUser);

        // reflection을 사용하여 user 필드 설정
        Boards updateBoards = new Boards(); // 기본 생성자 호출
        Field userField = Boards.class.getDeclaredField("user");
        userField.setAccessible(true);
        userField.set(updateBoards, ownerUser);

        when(userService.findUser(authUser.getUserId())).thenReturn(ownerUser);
        when(boardsRepository.findById(boardId)).thenReturn(Optional.of(updateBoards));
        // when
        BoardsSaveResponseDto responseDto = boardsService.updateBoards(authUser, boardId, requestDto);
        //then
        assertNotNull(requestDto);
        assertEquals(responseDto.getTitle(), requestDto.getTitle());
        assertEquals(responseDto.getContents(), requestDto.getContents());
        assertEquals(responseDto.getImages(), requestDto.getImages());
    }
}
