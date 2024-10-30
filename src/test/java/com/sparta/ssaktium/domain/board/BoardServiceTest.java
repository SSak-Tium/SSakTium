package com.sparta.ssaktium.domain.board;

import com.sparta.ssaktium.domain.boards.dto.requestDto.BoardSaveRequestDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardDetailResponseDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardSaveResponseDto;
import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.enums.PublicStatus;
import com.sparta.ssaktium.domain.boards.enums.StatusEnum;
import com.sparta.ssaktium.domain.boards.repository.BoardImagesRepository;
import com.sparta.ssaktium.domain.boards.repository.BoardRepository;
import com.sparta.ssaktium.domain.boards.service.BoardService;
import com.sparta.ssaktium.domain.comments.entity.Comment;
import com.sparta.ssaktium.domain.comments.service.CommentService;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.common.service.S3Service;
import com.sparta.ssaktium.domain.friends.service.FriendService;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.enums.UserRole;
import com.sparta.ssaktium.domain.users.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserService userService;

    @Mock
    private FriendService friendService;

    @Mock
    private S3Service s3Service;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private BoardImagesRepository boardImagesRepository;

    @InjectMocks
    private BoardService boardService;

    @Test
    public void 보드_생성_성공() {
        //given
        AuthUser authUser = new AuthUser(1L,"aa@aa.com", UserRole.ROLE_USER);
        BoardSaveRequestDto requestDto = new BoardSaveRequestDto("aa","aaa", PublicStatus.ALL);
        // 임시 이미지 파일 생성 (Mocking MultipartFile)
        MultipartFile image = new MockMultipartFile("image", "test-image.jpg", "image/jpeg", "test image content".getBytes());
        List<MultipartFile> imageList = List.of(image);
        // S3 업로드 메서드 Mocking
        String mockImageUrl = "http://mock-s3-url/test-image.jpg";
        when(s3Service.uploadImageListToS3(anyList(), any())).thenReturn(List.of(mockImageUrl));

        Board mockBoard = new Board("aa", "aaa", PublicStatus.ALL, new User());
        ReflectionTestUtils.setField(mockBoard,"id",1L); // ID 설정

        when(boardRepository.save(any(Board.class))).thenReturn(mockBoard);

        //when
        BoardSaveResponseDto responseDto = boardService.saveBoards(authUser.getUserId(),requestDto,imageList);
        //then
        assertNotNull(responseDto);
        assertEquals(responseDto.getTitle(), requestDto.getTitle());
        assertEquals(responseDto.getContents(), requestDto.getContents());
        assertNotNull(imageList);
        assertEquals(1, imageList.size()); // 이미지 URL의 개수 확인
    }

//    @Test
//    public void 보드_수정_성공() throws IllegalAccessException, NoSuchFieldException, IOException {
//        //given
//        AuthUser authUser = new AuthUser(1L,"aa@aa.com", UserRole.ROLE_USER);
//        long boardId = 1L;
//        BoardSaveRequestDto oldrequestDto = new BoardSaveRequestDto("aa","aaa", PublicStatus.ALL);
//        BoardSaveRequestDto requestDto = new BoardSaveRequestDto("aa2","aaa2", PublicStatus.ALL);
//        // 게시글 소유자 생성
//        User ownerUser = new User("aa@aa.com", "password", "name", UserRole.ROLE_USER);
//        // 임시 이미지 파일 생성 (Mocking MultipartFile)
//        MultipartFile image = new MockMultipartFile("image", "test-image.jpg", "image/jpeg", "test image content".getBytes());
//
//        // S3 업로드 메서드 Mocking
//        String mockImageUrl = "http://mock-s3-url/test-image.jpg";
//        when(s3Service.uploadImageToS3(any(MultipartFile.class), any())).thenReturn(mockImageUrl);
//
//        // reflection을 사용하여 user 필드 설정
//        Board updateBoard = new Board(oldrequestDto, ownerUser, "http://mock-s3-url/test-image.jpg"); // 기본 생성자 호출
//        Field userField = Board.class.getDeclaredField("user");
//        userField.setAccessible(true);
//        userField.set(updateBoard, ownerUser);
//
//        when(userService.findUser(authUser.getUserId())).thenReturn(ownerUser);
//        when(boardRepository.findById(boardId)).thenReturn(Optional.of(updateBoard));
//        // when
//        BoardSaveResponseDto responseDto = boardService.updateBoards(authUser.getUserId(), boardId, requestDto,image);
//        //then
//        assertNotNull(requestDto);
//        assertEquals(responseDto.getTitle(), requestDto.getTitle());
//        assertEquals(responseDto.getContents(), requestDto.getContents());
//        assertEquals(responseDto.getImageUrl(), mockImageUrl);
//    }
//
//    @Test
//    public void 보드_단건조회_성공 () throws IOException {
//        //given
//        long boardId = 1L;
//        User user = new User("aa@aa.com","Qq123456!","aa", "1990",UserRole.ROLE_USER);
//        BoardSaveRequestDto requestDto = new BoardSaveRequestDto("aa","aaa", PublicStatus.ALL);
//
//        // S3 업로드 메서드 Mocking
//        String mockImageUrl = "http://mock-s3-url/test-image.jpg";
//
//        Board board = new Board(requestDto,user,mockImageUrl);
//        ReflectionTestUtils.setField(board,"id",1L);
//
//        List<Comment> mockComments = Arrays.asList(
//                new Comment("aa", board,user /* 기타 필요한 필드들 */),
//                new Comment("aa", board,user /* 기타 필요한 필드들 */)
//        );
//        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
//        when(commentService.findAllByBoardId(boardId)).thenReturn(mockComments);
//        //when
//        BoardDetailResponseDto responseDto = boardService.getBoard(boardId);
//        //then
//        assertNotNull(responseDto);
//        assertEquals(board.getId(), responseDto.getId());
//        assertEquals(board.getTitle(), responseDto.getTitle());
//        assertEquals(board.getContent(), responseDto.getContents());
//
//        // 댓글 검증
//        assertNotNull(responseDto.getComments());
//        assertEquals(mockComments.size(), responseDto.getComments().size());
//        assertEquals(mockComments.get(0).getContent(), responseDto.getComments().get(0).getContent());
//        assertEquals(mockComments.get(1).getContent(), responseDto.getComments().get(1).getContent());
//    }
//
//    @Test
//    public void 내_게시글_찾기() {
//        // given
//        AuthUser authUser = new AuthUser(1L, "aa@aa.com", UserRole.ROLE_USER);
//        User user = new User("aa@aa.com", "aa", "Qq123456!", "1990",  UserRole.ROLE_USER);
//        BoardSaveRequestDto requestDto = new BoardSaveRequestDto("aa","aaa", PublicStatus.ALL);
//        String mockImageUrl = "http://mock-s3-url/test-image.jpg";
//        // Mocking
//        when(userService.findUser(authUser.getUserId())).thenReturn(user);
//
//        Board board1 = new Board(requestDto, user, "http://mock-s3-url/test-image1.jpg");
//        ReflectionTestUtils.setField(board1, "id", 1L);
//
//        Board board2 = new Board(requestDto,  user, "http://mock-s3-url/test-image2.jpg");
//        ReflectionTestUtils.setField(board2, "id", 2L);
//
//        Board board3 = new Board(requestDto,  user, "http://mock-s3-url/test-image3.jpg");
//        ReflectionTestUtils.setField(board3, "id", 3L);
//
//
//        // Mock Page 객체
//        List<Board> mockBoards = Arrays.asList(board1, board2, board3);
//        Page<Board> mockPage = new PageImpl<>(mockBoards, PageRequest.of(0, 5), mockBoards.size());
//
//        when(boardRepository.findAllByUserIdAndStatusEnum(user.getId(), StatusEnum.ACTIVATED, PageRequest.of(0, 5)))
//                .thenReturn(mockPage);
//
//        // 댓글 2개를 board1에 추가
//        Comment comment1 = new Comment("댓글 내용 1", board1,user);
//        ReflectionTestUtils.setField(comment1, "id", 1L);
//
//        Comment comment2 = new Comment("댓글 내용 2",board1, user);
//        ReflectionTestUtils.setField(comment2, "id", 2L);
//
//        when(commentService.findAllByBoardId(board1.getId())).thenReturn(Arrays.asList(comment1, comment2));
//
//        // when
//        BoardPageResponseDto responseDto = boardService.getMyBoards(authUser.getUserId(), 1, 5);
//
//        // then
//        assertNotNull(responseDto);
//        assertEquals(5, responseDto.getSize());
//        assertEquals(1, responseDto.getTotalPages()); // 총 페이지 수
//        assertEquals(mockBoards.size(), responseDto.getTotalElements());
//
//    }

}



