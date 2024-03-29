package com.coffee.americanote.coffee.repository;

import com.coffee.americanote.coffee.domain.entity.CoffeeFlavour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeFlavourRepository extends JpaRepository<CoffeeFlavour, Long> {
}
