package com.coffee.americanote.user.repository;

import com.coffee.americanote.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
