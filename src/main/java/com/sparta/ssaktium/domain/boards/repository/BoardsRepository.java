package com.sparta.ssaktium.domain.boards.repository;

import com.sparta.ssaktium.domain.boards.entity.Boards;
import com.sparta.ssaktium.domain.boards.entity.PublicStatus;
import com.sparta.ssaktium.domain.boards.entity.StatusEnum;
import com.sparta.ssaktium.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardsRepository extends JpaRepository<Boards,Long> {

    Page<Boards> findAllByUserIdAndStatusEnum(Long id, StatusEnum statusEnum, Pageable pageable);

    @Query("SELECT b FROM Boards b WHERE (b.user = :user) OR " +
            "(b.publicStatus = :friendsStatus AND b.user IN :friends) OR " +
            "(b.publicStatus = :allStatus) " +
            "ORDER BY b.createdAt DESC") // createdDate를 기준으로 내림차순 정렬
    Page<Boards> findAllForNewsFeed(
            @Param("user") User user,
            @Param("friends") List<User> friends,
            @Param("friendsStatus") PublicStatus friendsStatus,
            @Param("allStatus") PublicStatus allStatus,
            Pageable pageable
    );
}
