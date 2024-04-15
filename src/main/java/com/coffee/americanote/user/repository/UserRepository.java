package com.coffee.americanote.user.repository;

import com.coffee.americanote.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.flavours WHERE u.id = :userId")
    Optional<User> findByIdWithFlavours(@Param("userId") Long userId);
}