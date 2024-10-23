package com.sparta.springusersetting.domain.friends.repository;


import com.sparta.springusersetting.domain.friends.entity.Friends;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friends, Long> {

    @Query("SELECT i FROM Friends i WHERE i.userId.id = :userId AND i.friendUserId.id = :friendUserId")
    Optional<Friends> findByUserIdAndFriendUserId(Long userId, Long friendUserId);

    @Query("SELECT f FROM Friends f WHERE f.userId.id = :userId OR f.friendUserId.id = :friendId")
    Page<Friends> findByUserIdOrFriendId(@Param("userId") Long userId, @Param("friendId") Long friendId, Pageable pageable);

}