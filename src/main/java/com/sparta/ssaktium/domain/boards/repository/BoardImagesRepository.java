package com.sparta.ssaktium.domain.boards.repository;

import com.sparta.ssaktium.domain.boards.entity.BoardImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardImagesRepository extends JpaRepository<BoardImages, Long> {

    @Modifying
    @Query("DELETE FROM BoardImages bi WHERE bi.board.id = :boardId")
    void deleteByBoardId(@Param("boardId") Long boardId);
}
