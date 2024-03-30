package com.coffee.americanote.user.repository;

import com.coffee.americanote.user.domain.entity.User;
import com.coffee.americanote.user.domain.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select ut from User u left outer join UserToken ut on u.id = ut.userId where u.kakaoId = :kakaoId")
    Optional<UserToken> findByKakaoId(@Param("kakaoId") Long kakaoId);
}