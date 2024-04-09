package com.coffee.americanote.cafe.service;

import com.coffee.americanote.cafe.domain.CafeWithHasLike;
import com.coffee.americanote.cafe.domain.entity.Cafe;
import com.coffee.americanote.cafe.domain.entity.RecentSearch;
import com.coffee.americanote.cafe.domain.request.SearchCafeRequest;
import com.coffee.americanote.cafe.domain.response.CafeDetailResponse;
import com.coffee.americanote.cafe.domain.response.CafePreviewResponse;
import com.coffee.americanote.cafe.domain.response.CafeResponse;
import com.coffee.americanote.cafe.domain.response.CafeSearchResponse;
import com.coffee.americanote.cafe.repository.CafeRepository;
import com.coffee.americanote.cafe.repository.RecentSearchRepository;
import com.coffee.americanote.cafe.repository.querydsl.CafeQueryRepository;
import com.coffee.americanote.coffee.domain.entity.Coffee;
import com.coffee.americanote.coffee.repository.CoffeeRepository;
import com.coffee.americanote.common.entity.ErrorCode;
import com.coffee.americanote.common.validator.CommonValidator;
import com.coffee.americanote.review.domain.entity.Review;
import com.coffee.americanote.review.repository.ReviewRepository;
import com.coffee.americanote.security.jwt.util.JwtTokenProvider;
import com.coffee.americanote.user.domain.entity.User;
import com.coffee.americanote.user.domain.entity.UserFlavour;
import com.coffee.americanote.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.Map.Entry;

@Slf4j
@RequiredArgsConstructor
@Service
public class CafeService {

    private final CafeRepository cafeRepository;
    private final CoffeeRepository coffeeRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CafeQueryRepository cafeQueryRepository;
    private final RecentSearchRepository recentSearchRepository;

    public List<CafeResponse> getAllCafes() {
        List<CafeResponse> allCafe = new ArrayList<>();
        for (Cafe cafe : cafeRepository.findAll()) {
            allCafe.add(new CafeResponse(cafe.getId(), cafe.getLatitude(), cafe.getLongitude()));
        }
        return allCafe;
    }

    public Set<CafeResponse> searchCafeByFiltering(SearchCafeRequest request) {
        return cafeQueryRepository.getAllFilteringCafe(request);
    }

    public CafeDetailResponse getCafeDetail(Long cafeId, String token) {
        CommonValidator.notNullOrThrow(token, ErrorCode.EMPTY_TOKEN.getErrorMessage());
        Long userId = jwtTokenProvider.getUserId(token);
        return cafeQueryRepository.getCafeDetail(cafeId, userId);
    }

    public List<CafePreviewResponse> getRecommendCafes(String token) {
        CommonValidator.notNullOrThrow(token, ErrorCode.EMPTY_TOKEN.getErrorMessage());
        Long userId = jwtTokenProvider.getUserId(token);
        Optional<User> findUser = userRepository.findByIdWithFlavours(userId);
        CommonValidator.notNullOrThrow(findUser, ErrorCode.NOT_FOUND_USER.getErrorMessage());
        User user = findUser.get();

        List<UserFlavour> userFlavours = user.getFlavours();

        // 모든 커피 정보 가져오기
        List<Coffee> allCoffeeData = coffeeRepository.findAllWithFlavoursAndCafe();

        // 각 커피에 대한 우선순위 부여
        HashMap<Coffee, Double> priorityMap = calculatePriorities(user, userFlavours, allCoffeeData);

        // 5명 뽑기
        List<Coffee> topCoffees = selectTopPriorityCoffees(priorityMap);

        // response로 변환 후 반환
        return createRecommendCafeResponseList(topCoffees, user);
    }

    private HashMap<Coffee, Double> calculatePriorities(User user, List<UserFlavour> userFlavours,
                                                        List<Coffee> allCoffeeData) {
        HashMap<Coffee, Double> priorityMap = new HashMap<>();
        allCoffeeData.forEach(coffee -> {
            priorityMap.put(coffee, calculatePriorityForMatchingAttributes(coffee, user, userFlavours));
        });
        return priorityMap;
    }

    private double calculatePriorityForMatchingAttributes(Coffee coffee, User user,
                                                           List<UserFlavour> userFlavours) {
        // 향 일치 개수
        double priority = coffee.getFlavours().stream()
                .flatMap(flavour -> userFlavours.stream()
                        .map(UserFlavour::getFlavour)
                        .filter(userFlavour -> userFlavour.equals(flavour.getFlavour())))
                        .count();
        // 강도 일치하면 +1 / 1단계 차이나면 +0.5 / 2단계 차이나면 +0
        priority += user.getIntensity() != null ? 1 -
                Math.abs(coffee.getIntensity().getWeight() - user.getIntensity().getWeight()) : 0;
        // 산미 일치하면 +1 / 1단계 차이나면 +0.5 / 2단계 차이나면 +0
        priority += user.getAcidity() != null ? 1 -
                Math.abs(coffee.getAcidity().getWeight() - user.getAcidity().getWeight()) : 0;

        return priority;
    }

    private List<Coffee> selectTopPriorityCoffees(Map<Coffee, Double> priorityMap) {
        Comparator<Map.Entry<Coffee, Double>> randomComparator =
                Entry.<Coffee, Double>comparingByValue()
                .thenComparing(entry -> Math.random());

        return priorityMap.entrySet()
                .stream()
                .sorted(randomComparator.reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();
    }

    private List<CafePreviewResponse> createRecommendCafeResponseList(List<Coffee> topCoffees, User user) {
        List<CafePreviewResponse> result = new ArrayList<>();

        List<Cafe> cafes = topCoffees.stream().map(Coffee::getCafe).toList();
        List<CafeWithHasLike> cafeWithLike = cafeQueryRepository.findCafesWithLike(topCoffees, user);
        List<Review> reviews = reviewRepository.findAllByCafeIn(cafes);

        int coffeeIndex = 0;
        for (CafeWithHasLike cafe : cafeWithLike) {
            List<Review> cafeReviews = reviews.stream()
                    .filter(review -> review.getCafe().equals(cafe.getCafe()))
                    .toList();
            result.add(new CafePreviewResponse(cafe.getCafe(), topCoffees.get(coffeeIndex++), cafeReviews, cafe.getHasLike()));
        }
        return result;
    }

    @Transactional
    public List<CafeSearchResponse> getAllSearchCafe(String keyword, String accessToken) {
        Long userId = accessToken != null ? jwtTokenProvider.getUserId(accessToken) : 0;
        if (userId != 0) {
            saveSearchKeyword(keyword, userId);
        }
        return cafeQueryRepository.getAllSearchCafe(keyword, userId);
    }

    private void saveSearchKeyword(String keyword, Long userId) {
        if (!recentSearchRepository.existsByUserIdAndSearchWord(userId, keyword) &&
                !keyword.isBlank() && !keyword.equals("undefined")) {
            if (recentSearchRepository.countByUserId(userId) == 5) {
                List<RecentSearch> allByUserId = recentSearchRepository.findAllByUserIdOrderByCreatedDateAsc(userId);
                recentSearchRepository.delete(allByUserId.get(0));
            }
            recentSearchRepository.save(RecentSearch.toEntity(userId, keyword));
        }
    }

    public List<String> getAllRecentSearchWord(String accessToken) {
        Long userId = accessToken != null ? jwtTokenProvider.getUserId(accessToken) : 0;
        return recentSearchRepository.findAllByUserIdOrderByCreatedDateAsc(userId)
                .stream().map(RecentSearch::getSearchWord).toList();
    }

    @Transactional
    public void deleteRecentSearchWord(String keyword, String accessToken) {
        CommonValidator.notNullOrThrow(accessToken, ErrorCode.INVALID_TOKEN.getErrorMessage());
        CommonValidator.hasTextOrThrow(keyword, ErrorCode.NOT_FOUND_KEYWORD.getErrorMessage());
        Long userId = jwtTokenProvider.getUserId(accessToken);

        RecentSearch deleteEntity = recentSearchRepository.findByUserIdAndSearchWord(userId, keyword);
        recentSearchRepository.delete(deleteEntity);
    }
}
