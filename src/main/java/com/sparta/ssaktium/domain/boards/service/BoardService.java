package com.sparta.ssaktium.domain.boards.service;

import com.sparta.ssaktium.domain.boards.dto.requestDto.BoardSaveRequestDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardDetailResponseDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardPageResponseDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardSaveResponseDto;
import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.enums.PublicStatus;
import com.sparta.ssaktium.domain.boards.enums.StatusEnum;
import com.sparta.ssaktium.domain.boards.exception.NotFoundBoardException;
import com.sparta.ssaktium.domain.boards.exception.NotUserOfBoardException;
import com.sparta.ssaktium.domain.boards.repository.BoardRepository;
import com.sparta.ssaktium.domain.comments.dto.response.CommentSimpleResponseDto;
import com.sparta.ssaktium.domain.comments.entity.Comment;
import com.sparta.ssaktium.domain.comments.service.CommentService;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.common.service.S3Service;
import com.sparta.ssaktium.domain.friends.service.FriendService;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.enums.UserRole;
import com.sparta.ssaktium.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;
    private final FriendService friendService;
    private final S3Service s3Service;


    @Transactional
    public BoardSaveResponseDto saveBoards(Long userId, BoardSaveRequestDto requestDto, MultipartFile image) throws IOException {
        //유저 확인
        User user = userService.findUser(userId);
        // 업로드한 파일의 S3 URL 주소
        String imageUrl = s3Service.uploadImageToS3(image, s3Service.bucket);
        //제공받은 정보로 새 보드 만들기
        Board board = new Board(requestDto.getTitle(), requestDto.getContents(),requestDto.getPublicStatus() ,user, imageUrl);
        //저장
        Board savedBoard = boardRepository.save(board);
        //responseDto 반환
        return new BoardSaveResponseDto(savedBoard);
    }

    @Transactional
    public BoardSaveResponseDto updateBoards(Long userId, Long id, BoardSaveRequestDto requestDto, MultipartFile image) throws IOException {
        //유저 확인
        User user = userService.findUser(userId);
        //게시글 찾기
        Board updateBoard = getBoardById(id);
        //게시글 본인 확인
        if (!updateBoard.getUser().equals(user)) {
            throw new NotUserOfBoardException();
        }
        // 업로드한 파일의 S3 URL 주소
        String imageUrl = s3Service.uploadImageToS3(image, s3Service.bucket);
        //게시글 수정
        updateBoard.updateBoards(requestDto, imageUrl);
        boardRepository.save(updateBoard);
        //responseDto 반환
        return new BoardSaveResponseDto(updateBoard);
    }

    @Transactional
    public void deleteBoards(Long userId, Long id) {
        //유저 확인
        User user = userService.findUser(userId);
        //게시글 찾기
        Board deleteBoard = getBoardById(id);
        //어드민 일시 본인 확인 넘어가기
        if(!user.getUserRole().equals(UserRole.ROLE_ADMIN)) {
            //게시글 본인 확인
            if (!deleteBoard.getUser().equals(user)) {
                throw new NotUserOfBoardException();
            }
        }
        // 기존 등록된 URL 가지고 이미지 원본 이름 가져오기
        String imageUrl = s3Service.extractFileNameFromUrl(deleteBoard.getImageUrl());

        // 가져온 이미지 원본 이름으로 S3 이미지 삭제
        s3Service.s3Client.deleteObject(s3Service.bucket, imageUrl);
        //해당 보드 삭제 상태 변경
        deleteBoard.deleteBoards();
        boardRepository.save(deleteBoard);
    }

    //게시글 단건 조회
    public BoardDetailResponseDto getBoard(Long id) {
        //게시글 찾기
        Board board = getBoardById(id);
        //댓글 찾기
        List<Comment> commentList = getCommentsByBoardId(board.getId());

        List<CommentSimpleResponseDto> dtoList = new ArrayList<>();
        for (Comment comments : commentList) {
            dtoList.add(new CommentSimpleResponseDto(comments.getId(),
                    comments.getContent(),
                    comments.getModifiedAt(),
                    comments.getCommentLikesCount()));
        }

        return new BoardDetailResponseDto(board, dtoList);
    }

    public BoardPageResponseDto getMyBoards(Long userId, int page, int size) {
        //사용자 찾기
        User user = userService.findUser(userId);
        //페이지 요청 객체 생성 (페이지 숫자가 실제로는 0부터 시작하므로 원하는 숫자 -1을 입력해야 해당 페이지가 나온다)
        Pageable pageable = PageRequest.of(page - 1, size);
        //해당 유저가 쓴 게시글 페이지네이션해서 가져오기
        Page<Board> boards = boardRepository.findAllByUserIdAndStatusEnum(user.getId(), StatusEnum.ACTIVATED, pageable);

        // BoardsPageResponseDto 생성
        List<BoardDetailResponseDto> boardDetails = new ArrayList<>();
        for (Board board : boards) {
            // 댓글 리스트 가져오기
            List<Comment> commentList = getCommentsByBoardId(board.getId());
            List<CommentSimpleResponseDto> dtoList = new ArrayList<>();
            for (Comment comment : commentList) {
                dtoList.add(new CommentSimpleResponseDto(
                        comment.getId(),
                        comment.getContent(),
                        comment.getModifiedAt(),
                        comment.getCommentLikesCount()
                ));
            }
            // BoardDetailResponseDto 생성
            boardDetails.add(new BoardDetailResponseDto(board, dtoList));
        }

        // BoardsPageResponseDto 생성
        return new BoardPageResponseDto(
                boardDetails,
                boards.getTotalPages(),
                boards.getTotalElements(),
                boards.getSize(),
                boards.getNumber() + 1
        );
    }

    //뉴스피드
    public Page<BoardDetailResponseDto> getNewsfeed(Long userId, int page, int size) {
        //사용자 찾기
        User user = userService.findUser(userId);
        Pageable pageable = PageRequest.of(page - 1, size);

        //친구목록 가져오기
        List<User> friends = friendService.findFriends(user.getId());

        // 게시글 리스트 가져오기
        Page<Board> boardsPage = boardRepository.findAllForNewsFeed(
                user,
                friends,
                PublicStatus.FRIENDS, // 친구의 게시글 상태
                PublicStatus.ALL,      // 전체 공개 게시글 상태
                pageable               // Pageable 객체 전달
        );

        List<BoardDetailResponseDto> dtoList = new ArrayList<>();
        for (Board board : boardsPage) {
            // 댓글 리스트 가져오기
            List<Comment> commentList = getCommentsByBoardId(board.getId());
            List<CommentSimpleResponseDto> commentDtos = new ArrayList<>();
            for (Comment comment : commentList) {
                commentDtos.add(new CommentSimpleResponseDto(
                        comment.getId(),
                        comment.getContent(),
                        comment.getModifiedAt(),
                        comment.getCommentLikesCount()
                ));
            }
            // BoardDetailResponseDto 생성
            dtoList.add(new BoardDetailResponseDto(board, commentDtos));
        }
        return new PageImpl<>(dtoList, pageable, boardsPage.getTotalElements());
    }

    //Board 찾는 메서드
    public Board getBoardById(Long id) {
        return boardRepository.findActiveBoardById(id,StatusEnum.DELETED)
                .orElseThrow(NotFoundBoardException::new);
    }

    //Board에 연관된 댓글리스트 불러오기
    public List<Comment> getCommentsByBoardId(Long boardId) {
        return boardRepository.findCommentsByBoardId(boardId, StatusEnum.DELETED);
    }
}
