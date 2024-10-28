package com.sparta.ssaktium.domain.comments.repository;

import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.comments.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByBoard(Board board, Pageable pageable);

}
