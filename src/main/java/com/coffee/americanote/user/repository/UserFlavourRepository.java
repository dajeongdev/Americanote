package com.coffee.americanote.user.repository;

import com.coffee.americanote.user.domain.entity.User;
import com.coffee.americanote.user.domain.entity.UserFlavour;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFlavourRepository extends JpaRepository<UserFlavour, Long> {
    List<UserFlavour> findAllByUser(User user);
}
