package com.sparta.ssaktium.domain.boards.repository;

import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.enums.PublicStatus;
import com.sparta.ssaktium.domain.boards.enums.StatusEnum;
import com.sparta.ssaktium.domain.comments.entity.Comment;
import com.sparta.ssaktium.domain.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByUserIdAndStatusEnum(Long id, StatusEnum statusEnum, Pageable pageable);

    @Query("SELECT b FROM Board b WHERE " +
            "(b.user = :user) OR " +
            "(b.publicStatus = :friendsStatus AND b.user IN :friends) OR " +
            "(b.publicStatus = :allStatus AND b.user IN :friends) " +
            "ORDER BY b.modifiedAt DESC")
    Page<Board> findAllForNewsFeed(
            @Param("user") User user,
            @Param("friends") List<User> friends,
            @Param("friendsStatus") PublicStatus friendsStatus,
            @Param("allStatus") PublicStatus allStatus,
            Pageable pageable
    );

    @Query("SELECT b FROM Board b WHERE b.id = :id AND b.publicStatus <> :deletedStatus")
    Optional<Board> findActiveBoardById(@Param("id") Long id, @Param("deletedStatus") StatusEnum deletedStatus);

    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.comments WHERE b.id = :id AND b.publicStatus <> :deletedStatus")
    List<Comment> findCommentsByBoardId(@Param("id") Long id, @Param("deletedStatus") StatusEnum deletedStatus);

    @Query("SELECT b FROM Board b WHERE b.publicStatus = :publicStatus")
    Page<Board> findAllByPublicStatus(@Param("publicStatus") PublicStatus publicStatus,Pageable pageable);
}
