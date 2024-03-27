package com.coffee.americanote.user.repository;

import com.coffee.americanote.user.domain.entity.UserFlavour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFlavourRepository extends JpaRepository<UserFlavour, Long> {
}
