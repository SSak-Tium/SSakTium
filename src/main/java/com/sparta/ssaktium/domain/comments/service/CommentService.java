package com.sparta.ssaktium.domain.comments.service;

import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.service.BoardService;
import com.sparta.ssaktium.domain.comments.dto.request.CommentRequestDto;
import com.sparta.ssaktium.domain.comments.dto.response.CommentResponseDto;
import com.sparta.ssaktium.domain.comments.entity.Comment;
import com.sparta.ssaktium.domain.comments.exception.CommentOwnerMismatchException;
import com.sparta.ssaktium.domain.comments.exception.NotFoundCommentException;
import com.sparta.ssaktium.domain.comments.repository.CommentRepository;
import com.sparta.ssaktium.domain.likes.LikeRedisService;
import com.sparta.ssaktium.domain.likes.commentLikes.repository.CommentLikeRepository;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Getter
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final LikeRedisService likeRedisService;
    private final UserService userService;
    private final BoardService boardService;

    // 댓글 조회
    public Page<CommentResponseDto> getComments(Long boardId, int page, int size) {
        // 게시글이 있는지 확인
        Board board = boardService.getBoardById(boardId);

        // 페이지네이션 생성
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Comment> comments = commentRepository.findByBoard(board, pageable);

        // 댓글 리스트를 Dto 로 반환
        return comments.map(comment -> new CommentResponseDto(comment, getLikeCount(comment.getId().toString())));
    }

    // 댓글 등록
    @Transactional
    public CommentResponseDto postComment(Long userId, Long boardId, CommentRequestDto commentRequestDto) {
        // 댓글을 작성할 게시글이 있는지 확인
        Board board = boardService.getBoardById(boardId);

        // 댓글을 작성할 유저
        User user = userService.findUser(userId);

        // 댓글을 생성 후 저장
        Comment comment = new Comment(commentRequestDto.getContent(), board, user);
        commentRepository.save(comment);

        // 좋아요 수 레디스에서 반영
        int redisLikeCount = getLikeCount(comment.getId().toString());

        return new CommentResponseDto(comment, redisLikeCount);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long userId, Long boardId, Long commentId, CommentRequestDto commentRequestDto) {
        // 댓글을 수정할 게시글이 있는지 확인
        boardService.getBoardById(boardId);

        // 수정할 댓글이 있는지 확인
        Comment comment = findComment(commentId);

        // 해당 댓글 작성자인지 확인
        if (!comment.getUser().getId().equals(userId)) {
            throw new CommentOwnerMismatchException();
        }

        // 댓글 내용 수정 후 저장
        comment.updateComment(commentRequestDto.getContent());
        commentRepository.save(comment);

        // 좋아요 수 레디스에서 반영
        int redisLikeCount = getLikeCount(comment.getId().toString());

        return new CommentResponseDto(comment, redisLikeCount);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        // 삭제할 댓글이 있는지 확인
        Comment comment = findComment(commentId);

        // 해당 댓글 또는 해당 게시글 작성자인지 확인
        if (!comment.getUser().getId().equals(userId) || !comment.getBoard().getUser().getId().equals(userId)) {
            throw new CommentOwnerMismatchException();
        }

        // 댓글 삭제
        commentRepository.delete(comment);
    }

    // 아이디로 댓글 찾기 메서드
    public Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(NotFoundCommentException::new);
    }

    // Redis 로 좋아요 수 조회하는 메서드
    public int getLikeCount(String commentId) {
        int redisCount = likeRedisService.getRedisLikeCount(likeRedisService.TARGET_TYPE_COMMENT, commentId);
        if (redisCount == 0) {
            return commentLikeRepository.countByCommentId(Long.valueOf(commentId));
        }
        return redisCount;
    }
}
