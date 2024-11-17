package com.sparta.ssaktium.domain.likes;

import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.exception.NotFoundBoardException;
import com.sparta.ssaktium.domain.boards.repository.BoardRepository;
import com.sparta.ssaktium.domain.comments.entity.Comment;
import com.sparta.ssaktium.domain.comments.exception.NotFoundCommentException;
import com.sparta.ssaktium.domain.comments.repository.CommentRepository;
import com.sparta.ssaktium.domain.likes.boardLikes.entity.BoardLike;
import com.sparta.ssaktium.domain.likes.boardLikes.repository.BoardLikeRepository;
import com.sparta.ssaktium.domain.likes.commentLikes.entity.CommentLike;
import com.sparta.ssaktium.domain.likes.commentLikes.repository.CommentLikeRepository;
import com.sparta.ssaktium.domain.likes.exception.AlreadyLikedException;
import com.sparta.ssaktium.domain.likes.exception.NotFoundBoardLikeException;
import com.sparta.ssaktium.domain.likes.exception.NotFoundCommentLikeException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class LikeDbService {

    private final BoardLikeRepository boardLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    // 좋아요 추가
    @Transactional
    public void saveLike(String targetType, String targetId, String userId) {
        // 타입 변경
        Long objectId = Long.valueOf(targetId);
        Long objectUserId = Long.valueOf(userId);

        if (targetType.equals("board")) {
            // 게시글이 있는지 확인.
            Board board = boardRepository.findById(objectId).
                    orElseThrow(() -> new NotFoundBoardException());

            // 이미 좋아요를 눌렀는지 확인.
            if (boardLikeRepository.existsByBoardIdAndUserId(objectId, objectUserId)) {
                throw new AlreadyLikedException();
            }

            BoardLike boardLike = new BoardLike(board, objectUserId);
            boardLikeRepository.save(boardLike);
        }

        if (targetType.equals("comment")) {
            Comment comment = commentRepository.findById(objectId).
                    orElseThrow(() -> new NotFoundCommentException());

            if (commentLikeRepository.existsByCommentIdAndUserId(objectId, objectUserId)) {
                throw new AlreadyLikedException();
            }

            CommentLike commentLike = new CommentLike(comment, objectUserId);
            commentLikeRepository.save(commentLike);
        }
    }

    // 좋아요 취소
    @Transactional
    public void deleteLike(String targetType, String targetId, String userId) {
        Long objectId = Long.valueOf(targetId);
        Long objectUserId = Long.valueOf(userId);

        if (targetType.equals("board")) {
            //좋아요 존재하는지 확인 + 삭제하기 위함
            BoardLike boardLike = boardLikeRepository.
                    findByBoardIdAndUserId(objectId, objectUserId).
                    orElseThrow(() -> new NotFoundBoardLikeException());

            boardLikeRepository.delete(boardLike);
        }

        if (targetType.equals("comment")) {
            CommentLike commentLike = commentLikeRepository.
                    findByCommentIdAndUserId(objectId, objectUserId).
                    orElseThrow(() -> new NotFoundCommentLikeException());

            commentLikeRepository.delete(commentLike);
        }
    }
}
