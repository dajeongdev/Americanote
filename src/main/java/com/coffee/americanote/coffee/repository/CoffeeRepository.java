package com.coffee.americanote.coffee.repository;

import com.coffee.americanote.coffee.domain.entity.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
}
