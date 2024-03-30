package com.coffee.americanote.user.repository;

import com.coffee.americanote.user.domain.entity.RecentSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentSearchRepository extends JpaRepository<RecentSearch, Long> {
}
