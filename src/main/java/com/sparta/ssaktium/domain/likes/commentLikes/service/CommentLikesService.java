package com.sparta.ssaktium.domain.likes.commentLikes.service;

import com.sparta.ssaktium.config.ApiResponse;
import com.sparta.ssaktium.domain.comments.entity.Comment;
import com.sparta.ssaktium.domain.comments.repository.CommentRepository;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.likes.commentLikes.dto.CommentLikesReponseDto;
import com.sparta.ssaktium.domain.likes.commentLikes.entity.CommentLikes;
import com.sparta.ssaktium.domain.likes.commentLikes.repository.CommentLikesRepository;
import com.sparta.ssaktium.domain.likes.exception.AlreadyLikedException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentLikesService {

    private final CommentRepository commentRepository;
    private final CommentLikesRepository commentLikesRepository;

    // 댓글에 좋아요 등록
    @Transactional
    public CommentLikesReponseDto postCommentLikes(Long commentId, AuthUser authUser) {
        // 댓글이 있는지 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new RuntimeException(""));

        // 좋아요를 이미 누른 댓글인지 확인
        if(commentLikesRepository.existsByCommentIdandUserId(commentId,authUser.getUserId())){
            throw new AlreadyLikedException();
        }

        // 좋아요 등록
        CommentLikes commentLikes = new CommentLikes(comment,authUser.getUserId());
        commentLikesRepository.save(commentLikes);

        // 댓글에 등록된 좋아요 수 증가
        comment.incrementLikesCount();
        commentRepository.save(comment);

        return new CommentLikesReponseDto(commentId,comment.getCommentLikesCount());
    }
}
