package com.sparta.springusersetting.domain.boards.repository;

import com.sparta.springusersetting.domain.boards.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
