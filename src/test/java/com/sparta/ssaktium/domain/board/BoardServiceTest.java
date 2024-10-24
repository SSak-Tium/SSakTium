package com.sparta.ssaktium.domain.board;

import com.sparta.ssaktium.domain.boards.dto.requestDto.BoardSaveRequestDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardSaveResponseDto;
import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.enums.PublicStatus;
import com.sparta.ssaktium.domain.boards.repository.BoardRepository;
import com.sparta.ssaktium.domain.boards.service.BoardService;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.enums.UserRole;
import com.sparta.ssaktium.domain.users.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private BoardService boardService;

    @Test
    public void 보드_생성_성공(){
        //given
        AuthUser authUser = new AuthUser(1L,"aa@aa.com", UserRole.ROLE_USER);
        BoardSaveRequestDto requestDto = new BoardSaveRequestDto("aa","aaa","aaaa", PublicStatus.ALL);


        //when
        BoardSaveResponseDto responseDto = boardService.saveBoards(authUser,requestDto);
        //then
        assertNotNull(requestDto);
        assertEquals(responseDto.getTitle(), requestDto.getTitle());
        assertEquals(responseDto.getContents(), requestDto.getContents());
        assertEquals(responseDto.getImages(), requestDto.getImages());
    }

    @Test
    public void 보드_수정_성공() throws IllegalAccessException, NoSuchFieldException {
        //given
        AuthUser authUser = new AuthUser(1L,"aa@aa.com", UserRole.ROLE_USER);
        long boardId = 1L;
        BoardSaveRequestDto requestDto = new BoardSaveRequestDto("aa","aaa","aaaa", PublicStatus.ALL);
        // 게시글 소유자 생성
        User ownerUser = new User("aa@aa.com", "password", "name", UserRole.ROLE_USER);

        // reflection을 사용하여 user 필드 설정
        Board updateBoard = new Board(); // 기본 생성자 호출
        Field userField = Board.class.getDeclaredField("user");
        userField.setAccessible(true);
        userField.set(updateBoard, ownerUser);

        when(userService.findUser(authUser.getUserId())).thenReturn(ownerUser);
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(updateBoard));
        // when
        BoardSaveResponseDto responseDto = boardService.updateBoards(authUser, boardId, requestDto);
        //then
        assertNotNull(requestDto);
        assertEquals(responseDto.getTitle(), requestDto.getTitle());
        assertEquals(responseDto.getContents(), requestDto.getContents());
        assertEquals(responseDto.getImages(), requestDto.getImages());
    }

//    @Test
//    public void 보드_삭제상태_성공 () throws NoSuchFieldException, IllegalAccessException {
//        //given
//        AuthUser authUser = new AuthUser(1L,"aa@aa.com", UserRole.ROLE_USER);
//        Users user = userService.findUser(authUser.getUserId());
//        long boardId = 1L;
//        BoardsSaveRequestDto requestDto = new BoardsSaveRequestDto("aa","aaa","aaaa", PublicStatus.ALL);
//        Boards boards = new Boards(requestDto,user);
//
//        Field idField = Boards.class.getDeclaredField("id");
//        idField.setAccessible(true);
//        idField.set(boards, boardId);
//        // when
//        boardsService.deleteBoards(authUser,boardId);
//        //then
//        assertNotNull(boards);
//        assertEquals(boards.getStatusEnum(), StatusEnum.DELETED);
//
//    }


}
