package com.sparta.ssaktium.domain.board;

import com.sparta.ssaktium.domain.boards.dto.requestDto.BoardSaveRequestDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardDetailResponseDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardSaveResponseDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardUpdateImageDto;
import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.entity.BoardImages;
import com.sparta.ssaktium.domain.boards.enums.PublicStatus;
import com.sparta.ssaktium.domain.boards.enums.StatusEnum;
import com.sparta.ssaktium.domain.boards.repository.BoardImagesRepository;
import com.sparta.ssaktium.domain.boards.repository.BoardRepository;
import com.sparta.ssaktium.domain.boards.service.BoardService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
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
        AuthUser authUser = new AuthUser(1L, "aa@aa.com", UserRole.ROLE_USER);
        BoardSaveRequestDto requestDto = new BoardSaveRequestDto("aa", "aaa", PublicStatus.ALL);
        // 임시 이미지 파일 생성 (Mocking MultipartFile)
        MultipartFile image = new MockMultipartFile("image", "test-image.jpg", "image/jpeg", "test image content".getBytes());
        List<MultipartFile> imageList = List.of(image);
        // S3 업로드 메서드 Mocking
        String mockImageUrl = "http://mock-s3-url/test-image.jpg";
        when(s3Service.uploadImageListToS3(anyList(), any())).thenReturn(List.of(mockImageUrl));

        Board mockBoard = new Board("aa", "aaa", PublicStatus.ALL, new User());
        ReflectionTestUtils.setField(mockBoard, "id", 1L); // ID 설정

        when(boardRepository.save(any(Board.class))).thenReturn(mockBoard);

        //when
        BoardSaveResponseDto responseDto = boardService.saveBoards(authUser.getUserId(), requestDto, imageList);
        //then
        assertNotNull(responseDto);
        assertEquals(responseDto.getTitle(), requestDto.getTitle());
        assertEquals(responseDto.getContents(), requestDto.getContents());
        assertNotNull(imageList);
        assertEquals(1, imageList.size()); // 이미지 URL의 개수 확인
    }

    @Test
    public void 보드_이미지_수정_성공() {
        //given
        AuthUser authUser = new AuthUser(1L, "aa@aa.com", UserRole.ROLE_USER);
        long boardId = 1L;
        // 게시글 소유자 생성
        User ownerUser = new User("aa@aa.com", "password", "name", UserRole.ROLE_USER);

        Board tempBoard = new Board("aa", "aaa", PublicStatus.ALL, ownerUser);
        ReflectionTestUtils.setField(tempBoard, "id", 1L);

        List<BoardImages> existingImages = new ArrayList<>();
        existingImages.add(new BoardImages("oldImageUrl1", tempBoard));
        existingImages.add(new BoardImages("oldImageUrl2", tempBoard));
        ReflectionTestUtils.setField(tempBoard, "imageUrls", existingImages);

        when(userService.findUser(authUser.getUserId())).thenReturn(ownerUser);
        when(boardRepository.findByIdAndStatusEnum(boardId, StatusEnum.ACTIVATED)).thenReturn(Optional.of(tempBoard));

        List<String> remainingImages = List.of("oldImageUrl1");
        List<MultipartFile> imageList = new ArrayList<>();
        MultipartFile newImage = new MockMultipartFile("image", "newImage.jpg", "image/jpeg", "new image content".getBytes());
        imageList.add(newImage);

        // S3 메서드 모킹
        String newImageUrl = "http://mock-s3-url/newImage.jpg";
        when(s3Service.uploadImageToS3(any(MultipartFile.class), any())).thenReturn(newImageUrl);

        // when
        BoardUpdateImageDto responseDto = boardService.updateImages(authUser.getUserId(), boardId, imageList, remainingImages);
        //then
        assertNotNull(responseDto);
        assertEquals(2, responseDto.getImageUrls().size()); // 남은 이미지 + 새로 추가된 이미지 확인
        assertTrue(responseDto.getImageUrls().contains("oldImageUrl1")); // 남은 이미지 확인
        assertTrue(responseDto.getImageUrls().contains(newImageUrl));
    }

    @Test
    public void 보드_본문_수정_성공() {
        //given
        AuthUser authUser = new AuthUser(1L, "aa@aa.com", UserRole.ROLE_USER);
        long boardId = 1L;
        BoardSaveRequestDto oldRequestDto = new BoardSaveRequestDto("aa", "aaa", PublicStatus.ALL);
        BoardSaveRequestDto updateRequestDto = new BoardSaveRequestDto("aa2", "aaa2", PublicStatus.ALL);
        // 게시글 소유자 생성
        User ownerUser = new User("aa@aa.com", "password", "name", UserRole.ROLE_USER);

        // reflection을 사용하여 user 필드 설정
        Board updateBoard = new Board(oldRequestDto.getTitle(), oldRequestDto.getContents(), oldRequestDto.getPublicStatus(), ownerUser); // 기본 생성자 호출
        ReflectionTestUtils.setField(updateBoard, "id", 1L);

        // BoardImages 객체 생성
        List<BoardImages> imageUrls = List.of(new BoardImages("aaa", updateBoard));
        ReflectionTestUtils.setField(updateBoard, "imageUrls", imageUrls);

        when(userService.findUser(authUser.getUserId())).thenReturn(ownerUser);
        when(boardRepository.findByIdAndStatusEnum(boardId, StatusEnum.ACTIVATED)).thenReturn(Optional.of(updateBoard));
        when(boardRepository.save(any(Board.class))).thenReturn(updateBoard);
        // when
        BoardSaveResponseDto responseDto = boardService.updateBoardContent(authUser.getUserId(), boardId, updateRequestDto);
        //then
        assertNotNull(responseDto);
        assertEquals(responseDto.getTitle(), updateRequestDto.getTitle());
        assertEquals(responseDto.getContents(), updateRequestDto.getContents());
    }

    @Test
    public void 보드_단건조회_성공() {
        //given
        long boardId = 1L;
        User user = new User("aa@aa.com", "Qq123456!", "aa", "1990", UserRole.ROLE_USER, "socialId");
        BoardSaveRequestDto requestDto = new BoardSaveRequestDto("aa", "aaa", PublicStatus.ALL);

        Board board = new Board(requestDto.getTitle(), requestDto.getContents(), requestDto.getPublicStatus(), user);
        ReflectionTestUtils.setField(board, "id", 1L);

        List<BoardImages> imageUrls = List.of(new BoardImages("aaa", board));
        ReflectionTestUtils.setField(board, "imageUrls", imageUrls);

        when(boardRepository.findByIdAndStatusEnum(boardId, StatusEnum.ACTIVATED)).thenReturn(Optional.of(board));
        when(boardRepository.countCommentsByBoardId(boardId)).thenReturn(4);
        //when
        BoardDetailResponseDto responseDto = boardService.getBoard(boardId);
        //then
        assertNotNull(responseDto);
        assertEquals(board.getId(), responseDto.getId());
        assertEquals(board.getTitle(), responseDto.getTitle());
        assertEquals(board.getContent(), responseDto.getContents());
    }

    @Test
    public void 사용자_게시글_조회_성공() {
        // given
        Long userId = 1L;
        User user = new User("aa@aa.com", "password", "name", UserRole.ROLE_USER);
        Board board = new Board("title", "content", PublicStatus.ALL, user);
        ReflectionTestUtils.setField(board, "id", 1L);

        List<BoardImages> boardImages = List.of(new BoardImages("http://example.com/image1.jpg", board));
        ReflectionTestUtils.setField(board, "imageUrls", boardImages);

        when(userService.findUser(userId)).thenReturn(user);
        when(boardRepository.findAllByUserIdAndStatusEnum(user.getId(), StatusEnum.ACTIVATED, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(board), PageRequest.of(0, 10), 1));
        when(boardRepository.countCommentsByBoardId(board.getId())).thenReturn(2);

        // when
        Page<BoardDetailResponseDto> response = boardService.getBoards(userId, "me", 1, 10);

        // then
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals("title", response.getContent().get(0).getTitle());
        assertEquals(2, response.getContent().get(0).getCommentCount());
        assertEquals(1, response.getContent().get(0).getImageUrls().size());
    }

    @Test
    public void 전체_게시글_조회_성공() {
        // given
        Long userId = 1L;
        User user = new User("aa@aa.com", "password", "name", UserRole.ROLE_USER);
        Board board = new Board("title", "content", PublicStatus.ALL, user);
        ReflectionTestUtils.setField(board, "id", 1L);

        List<BoardImages> boardImages = List.of(new BoardImages("http://example.com/image1.jpg", board));
        ReflectionTestUtils.setField(board, "imageUrls", boardImages);

        when(boardRepository.findAllByPublicStatus(PublicStatus.ALL, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(board), PageRequest.of(0, 10), 1));
        when(boardRepository.countCommentsByBoardId(board.getId())).thenReturn(2);

        // when
        Page<BoardDetailResponseDto> response = boardService.getBoards(userId, "all", 1, 10);

        // then
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals("title", response.getContent().get(0).getTitle());
        assertEquals(2, response.getContent().get(0).getCommentCount());
        assertEquals(1, response.getContent().get(0).getImageUrls().size());
    }

    @Test
    public void 게시글_삭제_성공 () {
        // given
        Long userId = 1L;
        Long boardId = 1L;

        User user = new User("user@aa.com", "password", "name", UserRole.ROLE_USER);
        Board board = new Board("Title", "Content", PublicStatus.ALL, user);
        ReflectionTestUtils.setField(board, "id", boardId);

        List<BoardImages> boardImages = List.of(new BoardImages("http://example.com/image1.jpg", board));
        ReflectionTestUtils.setField(board, "imageUrls", boardImages);

        // Mocking
        when(userService.findUser(userId)).thenReturn(user);
        when(boardRepository.findByIdAndStatusEnum(boardId, StatusEnum.ACTIVATED)).thenReturn(Optional.of(board));

        // when
        boardService.deleteBoards(userId, boardId);

        // then
        assertEquals(StatusEnum.DELETED, board.getStatusEnum()); // 게시글 상태가 삭제 상태로 변경되었는지 확인
        verify(boardRepository).save(board); // 게시글이 저장소에 저장되었는지 확인
    }

    @Test
    public void 게시글_삭제_성공_어드민() {
        // given
        Long userId = 1L;
        Long boardId = 1L;

        User adminUser = new User("admin@aa.com", "password", "admin", UserRole.ROLE_ADMIN);
        Board board = new Board("Title", "Content", PublicStatus.ALL, new User("other@aa.com", "password", "other", UserRole.ROLE_USER));
        ReflectionTestUtils.setField(board, "id", boardId);

        List<BoardImages> boardImages = List.of(new BoardImages("http://example.com/image1.jpg", board));
        ReflectionTestUtils.setField(board, "imageUrls", boardImages);

        // Mocking
        when(userService.findUser(userId)).thenReturn(adminUser);
        when(boardRepository.findByIdAndStatusEnum(boardId, StatusEnum.ACTIVATED)).thenReturn(Optional.of(board));

        // when
        boardService.deleteBoards(userId, boardId);

        // then
        assertEquals(StatusEnum.DELETED, board.getStatusEnum()); // 게시글 상태가 삭제 상태로 변경되었는지 확인
        verify(boardRepository).save(board); // 게시글이 저장소에 저장되었는지 확인
    }
}



