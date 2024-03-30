package com.coffee.americanote.cafe.repository;

import com.coffee.americanote.cafe.domain.entity.RecentSearch;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentSearchRepository extends JpaRepository<RecentSearch, Long> {
    int countByUserId(Long userId);

    List<RecentSearch> findAllByUserIdOrderByCreatedDateAsc(Long userId);

    boolean existsByUserIdAndSearchWord(Long userId, String searchWord);

    RecentSearch findByUserIdAndSearchWord(Long userId, String searchWord);
}
