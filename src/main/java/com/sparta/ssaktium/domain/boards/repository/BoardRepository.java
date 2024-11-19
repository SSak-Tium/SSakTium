package com.sparta.ssaktium.domain.boards.repository;

import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.enums.PublicStatus;
import com.sparta.ssaktium.domain.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByUserId(Long id, Pageable pageable);

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

    Optional<Board> findById(Long id);

    @Query("SELECT b FROM Board b WHERE b.publicStatus = :publicStatus")
    Page<Board> findAllByPublicStatus(@Param("publicStatus") PublicStatus publicStatus, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Board b JOIN b.comments c WHERE b.id = :boardId")
    int countCommentsByBoardId(@Param("boardId") Long boardId);

    @Query("SELECT b FROM Board b WHERE b.title LIKE %:keyword% OR b.content LIKE %:keyword%")
    Page<Board> searchBoardByTitleOrContent(String keyword, Pageable pageable);

    @Query("SELECT b.boardLikesCount FROM Board b WHERE b.id = :id")
    int findBoardLikesCountById(@Param("id") Long id);
}
