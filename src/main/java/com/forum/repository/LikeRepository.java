package com.forum.repository;

import com.forum.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

    @Transactional
    Long deleteByUserIdAndPostId(Long userId, Long postId);
}
