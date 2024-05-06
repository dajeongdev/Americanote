package com.coffee.americanote.like.service;

import com.coffee.americanote.like.domain.Like;
import com.coffee.americanote.like.repository.LikeRepository;
import com.coffee.americanote.security.jwt.util.UserIdProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;

    @Transactional
    public void toggleLike(Long cafeId) {
        final Long userId = UserIdProvider.getUserIdOrThrow();

        if (likeRepository.existsByUserIdAndCafeId(userId, cafeId)) {
            likeRepository.deleteByUserIdAndCafeId(userId, cafeId);
        } else {
            likeRepository.save(new Like(userId, cafeId));
        }
    }
}
