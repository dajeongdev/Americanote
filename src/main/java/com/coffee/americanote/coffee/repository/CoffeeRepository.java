package com.coffee.americanote.coffee.repository;

import com.coffee.americanote.coffee.domain.entity.Coffee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
    @Query("SELECT c FROM Coffee c JOIN FETCH c.flavours f JOIN FETCH c.cafe")
    List<Coffee> findAllWithFlavoursAndCafe();
}
