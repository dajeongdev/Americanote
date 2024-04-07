package com.coffee.americanote.review.repository;

import com.coffee.americanote.cafe.domain.entity.Cafe;
import com.coffee.americanote.review.domain.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByCafeIn(List<Cafe> cafes);
}
