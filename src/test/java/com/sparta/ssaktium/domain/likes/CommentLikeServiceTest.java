package com.sparta.ssaktium.domain.likes;

import com.sparta.ssaktium.domain.boards.exception.NotFoundBoardException;
import com.sparta.ssaktium.domain.comments.entity.Comment;
import com.sparta.ssaktium.domain.comments.exception.NotFoundCommentException;
import com.sparta.ssaktium.domain.comments.repository.CommentRepository;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.likes.commentLikes.dto.CommentLikeReponseDto;
import com.sparta.ssaktium.domain.likes.commentLikes.entity.CommentLike;
import com.sparta.ssaktium.domain.likes.commentLikes.repository.CommentLikeRepository;
import com.sparta.ssaktium.domain.likes.commentLikes.service.CommentLikeService;
import com.sparta.ssaktium.domain.likes.exception.AlreadyLikedException;
import com.sparta.ssaktium.domain.likes.exception.LikeOwnerMismatchException;
import com.sparta.ssaktium.domain.likes.exception.NotFoundCommentLikeException;
import com.sparta.ssaktium.domain.users.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentLikeServiceTest {

    // 실제 테스트할 서비스 객체
    @InjectMocks
    private CommentLikeService commentLikeService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @Test
    public void 댓글_좋아요_등록_성공() {
        // given
        Long userId = 1L;
        Long commentId = 1L;

        // Mock 설정: 댓글은 있는데 좋아요를 아직 안누른 경우
        Comment comment = new Comment();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentLikeRepository.existsByCommentIdAndUserId(commentId, userId)).thenReturn(false);

        // when: 댓글 좋아요 등록
        CommentLikeReponseDto response = commentLikeService.postCommentLike(commentId, userId);

        // then
        assertNotNull(response); // 응답이 null이 아님
        assertEquals(commentId, response.getCommentId()); // 댓글 ID 검증
        assertEquals(1, response.getCommentLikesCount()); // 좋아요 수 증가 검증
    }

    @Test
    void 댓글_좋아요_등록_실패_댓글이_존재하지_않음() {
        // given
        Long userId = 1L;
        Long commentId = 1L;

        // Mock 설정
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundCommentException.class, () -> commentLikeService.postCommentLike(userId, commentId));
    }

//    @Test
//    public void 댓글_좋아요_등록_실패_이미_좋아요를_누른_경우() {
//        // given
//        AuthUser authUser = new AuthUser(1L, "user@example.com", UserRole.ROLE_USER);
//        Long commentId = 1L;
//
//        // Mock 설정: 해당 댓글에 이미 좋아요 누른 경우
//        Comment comment = new Comment();
//        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
//        when(commentLikeRepository.existsByCommentIdAndUserId(commentId, authUser.getUserId())).thenReturn(true);
//
//        // when & then: 이미 좋아요를 누른 경우 예외 발생
//        assertThrows(AlreadyLikedException.class, () ->
//                commentLikeService.postCommentLike(commentId, authUser));
//    }

//    @Test
//    public void 댓글_좋아요_취소_성공() {
//        // given
//        AuthUser authUser = new AuthUser(1L, "user@example.com", UserRole.ROLE_USER);
//        Long commentId = 1L;
//        Long likeId = 1L;
//
//        // Mock 설정: 해당 댓글에 좋아요가 있는 경우
//        Comment comment = new Comment();
//        comment.incrementLikesCount(); // 초기 좋아요 수를 1로 설정
//        CommentLike commentLike = new CommentLike(comment, authUser.getUserId());
//
//        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
//        when(commentLikeRepository.existsByCommentIdAndUserId(commentId, authUser.getUserId())).thenReturn(true);
//        when(commentLikeRepository.findById(likeId)).thenReturn(Optional.of(commentLike));
//
//        // when: 댓글 좋아요 취소
//        commentLikeService.deleteCommentLike(commentId, likeId, authUser);
//
//        // then: 좋아요 수가 감소했는지 확인
//        assertEquals(0, comment.getCommentLikesCount()); // 좋아요 수 감소 검증
//    }
//
//    @Test
//    public void 댓글_좋아요_취소_실패_댓글이_존재하지_않는_경우() {
//        // given
//        AuthUser authUser = new AuthUser(1L, "user@example.com", UserRole.ROLE_USER);
//        Long commentId = 1L;
//        Long likeId = 1L;
//
//        // Mock 설정: 해당 댓글이 존재하지 않는 경우
//        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());
//
//        // when & then: 댓글이 존재하지 않는 경우 예외 발생
//        assertThrows(NotFoundCommentException.class, () ->
//                commentLikeService.deleteCommentLike(commentId, likeId, authUser));
//    }
//
//    @Test
//    public void 댓글_좋아요_취소_실패_좋아요가_존재하지_않는_경우() {
//        // given
//        AuthUser authUser = new AuthUser(1L, "user@example.com", UserRole.ROLE_USER);
//        Long commentId = 1L;
//        Long likeId = 1L;
//
//        // Mock 설정: 댓글은 있는데 좋아요가 없는 경우
//        Comment comment = new Comment();
//        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
//        when(commentLikeRepository.existsByCommentIdAndUserId(commentId, authUser.getUserId())).thenReturn(false);
//
//        // when & then: 좋아요가 존재하지 않는 경우 예외 발생
//        assertThrows(NotFoundCommentLikeException.class, () ->
//                commentLikeService.deleteCommentLike(commentId, likeId, authUser));
//    }
//
//    @Test
//    public void 댓글_좋아요_취소_좋아요_주인이_다른_경우() {
//        // given
//        AuthUser authUser = new AuthUser(1L, "user@example.com", UserRole.ROLE_USER);
//        Long commentId = 1L;
//        Long likeId = 1L;
//
//        // Mock 설정: 해당 댓글에 좋아요가 있는 경우 + 다른 사람이 취소 요청
//        Comment comment = new Comment();
//        CommentLike commentLike = new CommentLike(comment, 2L); // 다른 유저 ID
//
//        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
//        when(commentLikeRepository.existsByCommentIdAndUserId(commentId, authUser.getUserId())).thenReturn(true);
//        when(commentLikeRepository.findById(likeId)).thenReturn(Optional.of(commentLike));
//
//        // when & then: 좋아요 주인이 다른 경우 예외 발생
//        assertThrows(LikeOwnerMismatchException.class, () ->
//                commentLikeService.deleteCommentLike(commentId, likeId, authUser));
//    }
}
