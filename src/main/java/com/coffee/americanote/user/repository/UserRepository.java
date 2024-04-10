package com.coffee.americanote.user.repository;

import com.coffee.americanote.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByKakaoId(Long kakaoId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.flavours WHERE u.id = :userId")
    Optional<User> findByIdWithFlavours(@Param("userId") Long userId);
}