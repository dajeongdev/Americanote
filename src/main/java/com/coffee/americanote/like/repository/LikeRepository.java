package com.coffee.americanote.like.repository;

import com.coffee.americanote.like.domain.Like;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUserIdAndCafeId(Long userId, Long cafeId);

    void deleteByUserIdAndCafeId(Long userId, Long cafeId);

    List<Like> findByUserIdAndCafeIdIn(Long userId, List<Long> cafeIds);
}