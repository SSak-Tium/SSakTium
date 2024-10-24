package com.sparta.ssaktium.domain.boards.service;

import com.sparta.ssaktium.domain.boards.dto.requestDto.BoardSaveRequestDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardDetailResponseDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardPageResponseDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardSaveResponseDto;
import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.enums.PublicStatus;
import com.sparta.ssaktium.domain.boards.enums.StatusEnum;
import com.sparta.ssaktium.domain.boards.exception.NotFoundBoardException;
import com.sparta.ssaktium.domain.boards.repository.BoardRepository;
import com.sparta.ssaktium.domain.comments.dto.response.CommentResponseDto;
import com.sparta.ssaktium.domain.comments.dto.response.CommentSimpleResponseDto;
import com.sparta.ssaktium.domain.comments.entity.Comment;
import com.sparta.ssaktium.domain.comments.repository.CommentRepository;
import com.sparta.ssaktium.domain.comments.service.CommentService;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.friends.entity.FriendStatus;
import com.sparta.ssaktium.domain.friends.service.FriendService;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;
    private final CommentService commentService;
    private final FriendService friendService;


    @Transactional
    public BoardSaveResponseDto saveBoards(AuthUser authUser, BoardSaveRequestDto requestDto) {
        //유저 확인
        User user = userService.findUser(authUser.getUserId());
        //제공받은 정보로 새 보드 만들기
        Board newBoard = new Board(requestDto,user);
        //저장
        boardRepository.save(newBoard);
        //responseDto 반환
        return new BoardSaveResponseDto(newBoard);
    }

    @Transactional
    public BoardSaveResponseDto updateBoards(AuthUser authUser, Long id, BoardSaveRequestDto requestDto) {
        //유저 확인
        User user = userService.findUser(authUser.getUserId());
        //게시글 찾기
        Board updateBoard = findBoard(id);
        //게시글 본인 확인
       if(!updateBoard.getUser().equals(user)){
           throw new RuntimeException();
       }
       //게시글 수정
       updateBoard.updateBoards(requestDto);
       boardRepository.save(updateBoard);
       //responseDto 반환
       return new BoardSaveResponseDto(updateBoard);
    }

    @Transactional
    public void deleteBoards(AuthUser authUser,Long id){
        //유저 확인
        User user = userService.findUser(authUser.getUserId());
        //게시글 찾기
        Board deleteBoard = findBoard(id);
        //게시글 본인 확인
        if(!deleteBoard.getUser().equals(user)){
            throw new RuntimeException();
        }
        //해당 보드 삭제 상태 변경
        deleteBoard.deleteBoards();
        boardRepository.save(deleteBoard);
    }

    //게시글 단건 조회
    public BoardDetailResponseDto getBoard(Long id) {
        //게시글 찾기
       Board board = findBoard(id);
        //댓글 찾기
        List<Comment> commentList = commentService.findAllByBoardId(board.getId());

        List<CommentSimpleResponseDto> dtoList = new ArrayList<>();
        for(Comment comments: commentList){
            dtoList.add(new CommentSimpleResponseDto(comments.getId(),
                    comments.getContent(),
                    comments.getModifiedAt(),
                    comments.getCommentLikesCount()));
        }

        return  new BoardDetailResponseDto(board,dtoList);
    }

    public BoardPageResponseDto getMyBoards(AuthUser authUser, int page, int size) {
        //사용자 찾기
        User user = userService.findUser(authUser.getUserId());
        //페이지 요청 객체 생성 (페이지 숫자가 실제로는 0부터 시작하므로 원하는 숫자 -1을 입력해야 해당 페이지가 나온다)
        Pageable pageable = PageRequest.of(page -1, size);
        //해당 유저가 쓴 게시글 페이지네이션해서 가져오기
        Page<Board> boards = boardRepository.findAllByUserIdAndStatusEnum(user.getId(), StatusEnum.ACTIVATED,pageable);

        // BoardsPageResponseDto 생성
        List<BoardDetailResponseDto> boardDetails = new ArrayList<>();
        for (Board board : boards) {
            // 댓글 리스트 가져오기
            List<Comment> commentList = commentService.findAllByBoardId(board.getId());
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
                boards.getNumber()+1
        );
    }

    //뉴스피드
    public Page<BoardDetailResponseDto> getNewsfeed(AuthUser authUser, int page, int size) {
        //사용자 찾기
        User user = userService.findUser(authUser.getUserId());
        Pageable pageable = PageRequest.of(page - 1 ,size);

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
            List<Comment> commentList = commentService.findAllByBoardId(board.getId());
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
    public Board findBoard(long id){
       return boardRepository.findById(id).orElseThrow(NotFoundBoardException::new);
    }

}
