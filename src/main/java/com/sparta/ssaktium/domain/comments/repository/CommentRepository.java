package com.sparta.ssaktium.domain.comments.repository;

import com.sparta.ssaktium.domain.comments.entity.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comments, Long> {
    Page<Comments> findByBoardId(Long boardId, Pageable pageable);

}
