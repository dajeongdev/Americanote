package com.coffee.americanote.cafe.repository;

import com.coffee.americanote.cafe.domain.entity.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeRepository extends JpaRepository<Cafe, Long> {
}
