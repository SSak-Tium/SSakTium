package com.sparta.springusersetting.domain.boards.service;

import com.sparta.springusersetting.domain.boards.dto.requestDto.BoardsSaveRequestDto;
import com.sparta.springusersetting.domain.boards.dto.responseDto.BoardsDetailResponseDto;
import com.sparta.springusersetting.domain.boards.dto.responseDto.BoardsSaveResponseDto;
import com.sparta.springusersetting.domain.boards.entity.Boards;
import com.sparta.springusersetting.domain.boards.repository.BoardsRepository;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.repository.UserRepository;
import com.sparta.springusersetting.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comments;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardsService {

    private final BoardsRepository boardsRepository;
    private final UserService userService;


    @Transactional
    public BoardsSaveResponseDto saveBoards(AuthUser authUser, BoardsSaveRequestDto requestDto) {
        //유저 확인
        userService.findUser(authUser.getUserId());
        //제공받은 정보로 새 보드 만들기
        Boards newBoard = new Boards(requestDto);
        //저장
        boardsRepository.save(newBoard);
        //responseDto 반환
        return new BoardsSaveResponseDto(newBoard);
    }

    @Transactional
    public BoardsSaveResponseDto updateBoards(AuthUser authUser, Long id, BoardsSaveRequestDto requestDto) {
        //유저 확인
        User user = userService.findUser(authUser.getUserId());
        //게시글 찾기
        Boards updateBoards = findBoard(id);
        //게시글 본인 확인
       if(updateBoards.getUser().equals(user)){
           throw new RuntimeException();
       }
       //게시글 수정
       updateBoards.updateBoards(requestDto);
       boardsRepository.save(updateBoards);
       //responseDto 반환
       return new BoardsSaveResponseDto(updateBoards);
    }

    @Transactional
    public void deleteBoards(AuthUser authUser,Long id){
        //유저 확인
        User user = userService.findUser(authUser.getUserId());
        //게시글 찾기
        Boards deleteBoards = findBoard(id);
        //게시글 본인 확인
        if(deleteBoards.getUser().equals(user)){
            throw new RuntimeException();
        }
        //해당 보드 삭제 상태 변경
        deleteBoards.deleteBoards();
        boardsRepository.save(deleteBoards);
    }

//    //게시글 단건 조회
//    public BoardsDetailResponseDto getBoard(Long id) {
//        //게시글 찾기
//       Boards board = findBoard(id);
//        //댓글 찾기
//        List<Comments> commentList = commentRepository.findAllByBoardId(board.getId());
//
//        List<CommentResponseDto> dtoList = new ArrayList<>();
//        for(Comments comments: commentList){
//            dtoList.add(new CommentResponseDto(comments.getId,comments.getContents));
//        }
//
//        return  new BoardsDetailResponseDto(board,dtoList);
//    }


    //Board 찾는 메서드
    public Boards findBoard(long id){
       return boardsRepository.findById(id).orElseThrow(() -> new RuntimeException("aa"));
    }

}
