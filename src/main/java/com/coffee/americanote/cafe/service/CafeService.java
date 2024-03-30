package com.coffee.americanote.cafe.service;

import com.coffee.americanote.cafe.domain.entity.Cafe;
import com.coffee.americanote.cafe.domain.entity.RecentSearch;
import com.coffee.americanote.cafe.domain.request.SearchCafeRequest;
import com.coffee.americanote.cafe.domain.response.CafeDetailResponse;
import com.coffee.americanote.cafe.domain.response.CafePreviewResponse;
import com.coffee.americanote.cafe.domain.response.CafeResponse;
import com.coffee.americanote.cafe.domain.response.CafeSearchResponse;
import com.coffee.americanote.cafe.repository.CafeRepository;
import com.coffee.americanote.cafe.repository.querydsl.CafeQueryRepository;
import com.coffee.americanote.coffee.domain.entity.Coffee;
import com.coffee.americanote.coffee.repository.CoffeeRepository;
import com.coffee.americanote.common.entity.ErrorCode;
import com.coffee.americanote.common.exception.UserException;
import com.coffee.americanote.common.validator.CommonValidator;
import com.coffee.americanote.like.repository.LikeRepository;
import com.coffee.americanote.review.domain.entity.Review;
import com.coffee.americanote.review.repository.ReviewRepository;
import com.coffee.americanote.security.jwt.util.JwtTokenProvider;
import com.coffee.americanote.user.domain.entity.User;
import com.coffee.americanote.user.domain.entity.UserFlavour;
import com.coffee.americanote.cafe.repository.RecentSearchRepository;
import com.coffee.americanote.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CafeService {

    private final CafeRepository cafeRepository;
    private final CoffeeRepository coffeeRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CafeQueryRepository cafeQueryRepository;
    private final RecentSearchRepository recentSearchRepository;

    public List<CafeResponse> getAllCafe() {
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
        Boolean hasLike = Boolean.FALSE;
        if (token != null) {
            Long userId = jwtTokenProvider.getUserId(token);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

            if (likeRepository.existsByUserIdAndCafeId(user.getId(), cafeId)) {
                hasLike = Boolean.TRUE;
            }
        }

        Optional<Cafe> cafe = cafeRepository.findById(cafeId);
        CommonValidator.notNullOrThrow(cafe.orElse(null), ErrorCode.RESOURCE_NOT_FOUND.getErrorMessage());
        List<Review> reviews = reviewRepository.findAllByCafe(cafe.get());
        
        return new CafeDetailResponse(cafe.get(), reviews, hasLike);
    }

    public List<CafePreviewResponse> recommendCafes(String token) {
        Long userId = jwtTokenProvider.getUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        List<UserFlavour> userFlavours = user.getFlavours();

        // 모든 커피 정보 가져오기
        List<Coffee> allCoffeeData = coffeeRepository.findAll();

        // 각 커피에 대한 우선순위 부여
        HashMap<Coffee, Double> priorityMap = calculatePriorities(user, userFlavours, allCoffeeData);

        // 5명 뽑기
        List<Coffee> topCoffees = selectTopPriorityCoffees(priorityMap, 5);

        // response로 변환 후 반환
        return createRecCafeResponseList(topCoffees, user);
    }

    private HashMap<Coffee, Double> calculatePriorities(User user, List<UserFlavour> userFlavours,
                                                                 List<Coffee> allCoffeeData) {
        // 커피들의 우선순위를 저장하는 map
        HashMap<Coffee, Double> priorityMap = new HashMap<>();
        allCoffeeData.forEach(coffee -> {
            double priority = 0;
            // 여기서 향, 산미, 강도 일치하는지 확인하여 우선순위 계산
            priority += calculatePriorityForMatchingAttributes(coffee, user, userFlavours);
            priorityMap.put(coffee, priority);
        });
        return priorityMap;
    }

    private double calculatePriorityForMatchingAttributes(Coffee coffee, User user,
                                                           List<UserFlavour> userFlavours) {
        double priority = 0;
        // 향 일치 개수
        int matchingFlavours = (int) coffee.getFlavours().stream()
                .flatMap(flavour -> userFlavours.stream()
                        .map(UserFlavour::getFlavour)
                        .filter(userFlavour -> userFlavour.equals(flavour.getFlavour())))
                .count();
        priority += matchingFlavours;
        // 강도 일치하면 +1 / 1단계 차이나면 +0.5 / 2단계 차이나면 +0
        priority += user.getIntensity() != null ? 1 - Math.abs(
                coffee.getIntensity().getWeight()
                        - user.getIntensity().getWeight()) : 0;
        // 산미 일치하면 +1 / 1단계 차이나면 +0.5 / 2단계 차이나면 +0
        priority += user.getAcidity() != null ? 1 - Math.abs(
                coffee.getAcidity().getWeight()
                        - user.getAcidity().getWeight()) : 0;

        return priority;
    }

    private List<Coffee> selectTopPriorityCoffees(Map<Coffee, Double> priorityMap, long limitSize) {
        Comparator<Map.Entry<Coffee, Double>> randomComparator =
                Entry.<Coffee, Double>comparingByValue()
                .thenComparing(entry -> Math.random());

        return priorityMap.entrySet()
                .stream()
                .sorted(randomComparator.reversed())
                .limit(limitSize)
                .map(Map.Entry::getKey)
                .toList();
    }

    private List<CafePreviewResponse> createRecCafeResponseList(List<Coffee> topCoffees, User user) {
        List<CafePreviewResponse> result = new ArrayList<>();

        for (Coffee coffee : topCoffees) {
            // cafe
            Cafe cafe = coffee.getCafe();
            // reviews
            List<Review> reviews = reviewRepository.findAllByCafe(cafe);
            // hasLike
            boolean hasLike = likeRepository.existsByUserIdAndCafeId(user.getId(), coffee.getCafe().getId());
            result.add(new CafePreviewResponse(cafe, reviews, hasLike));
        }
        return result;
    }

    @Transactional
    public List<CafeSearchResponse> getAllSearchCafe(String keyword, String accessToken) {
        Long userId = accessToken != null ? jwtTokenProvider.getUserId(accessToken) : 0;
        // 토큰 있을 때 최근 검색어 등록
        // 최근 검색어에 이미 keyword가 있으면 등록X
        // keyword가 없으면 등록X
        if (userId != 0 && !recentSearchRepository.existsByUserIdAndSearchWord(userId, keyword) && !keyword.isEmpty()) {
            // 최근 검색어 5개일 경우 -> 제일 오래된 검색어 삭제 -> 새로운 검색어 저장
            if (recentSearchRepository.countByUserId(userId) == 5) {
                List<RecentSearch> allByUserId = recentSearchRepository
                        .findAllByUserIdOrderByCreatedDateAsc(userId);
                recentSearchRepository.delete(allByUserId.get(0));
            }
            // 최근 검색어 5개 미만일 경우 -> 새로운 검색어 저장
            recentSearchRepository.save(RecentSearch.toEntity(userId, keyword));
        }
        return cafeQueryRepository.getAllSearchCafe(keyword, userId);
    }
}
