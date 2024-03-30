package com.coffee.americanote.cafe.repository;

import com.coffee.americanote.cafe.domain.entity.RecentSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentSearchRepository extends JpaRepository<RecentSearch, Long> {
}
