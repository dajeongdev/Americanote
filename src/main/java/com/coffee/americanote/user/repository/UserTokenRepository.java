package com.coffee.americanote.user.repository;

import com.coffee.americanote.user.domain.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    Optional<UserToken> findByAccessToken(String accessToken);

    void deleteByUserId(Long userId);
}
