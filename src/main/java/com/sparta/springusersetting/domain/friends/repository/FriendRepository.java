package com.sparta.springusersetting.domain.friends.repository;


import com.sparta.springusersetting.domain.friends.entity.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friends, Long> {

    @Query("SELECT i FROM Friends i WHERE i.userId.id = :userId AND i.friendUserId.id = :friendUserId")
    Optional<Friends> findByUserIdAndFriendUserId(Long userId, Long friendUserId);


}