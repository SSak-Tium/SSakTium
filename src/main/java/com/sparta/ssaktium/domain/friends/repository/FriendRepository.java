package com.sparta.ssaktium.domain.friends.repository;


import com.sparta.ssaktium.domain.friends.entity.FriendStatus;
import com.sparta.ssaktium.domain.friends.entity.Friends;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friends, Long> {

    @Query("SELECT i FROM Friends i WHERE i.userId.id = :userId AND i.friendUserId.id = :friendUserId")
    Optional<Friends> findByUserIdAndFriendUserId(Long userId, Long friendUserId);


    @Query("SELECT f FROM Friends f WHERE (f.userId.id = :userId OR f.friendUserId.id = :friendId) AND f.friendStatus = :status")
    Page<Friends> findByUserIdOrFriendIdAndStatus(@Param("userId") Long userId,
                                                  @Param("friendId") Long friendId,
                                                  @Param("status") FriendStatus status,
                                                  Pageable pageable);

    @Query("SELECT f FROM Friends f WHERE (f.userId.id = :userId AND f.friendUserId.id = :friendId) OR (f.userId.id = :friendId AND f.friendUserId.id = :userId)")
    Optional<Friends> findByUserIdAndFriendId(@Param("userId") Long userId, @Param("friendId") Long friendId);

}