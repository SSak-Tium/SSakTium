package com.sparta.springusersetting.domain.boards.repository;

import com.sparta.springusersetting.domain.boards.entity.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardsRepository extends JpaRepository<Boards,Long> {
}
