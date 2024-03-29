package com.coffee.americanote.cafe.service;

import com.coffee.americanote.cafe.domain.entity.Cafe;
import com.coffee.americanote.cafe.domain.request.SearchCafeRequest;
import com.coffee.americanote.cafe.domain.response.CafeDetailResponse;
import com.coffee.americanote.cafe.domain.response.CafePreviewResponse;
import com.coffee.americanote.cafe.domain.response.CafeResponse;
import com.coffee.americanote.cafe.repository.CafeRepository;
import com.coffee.americanote.coffee.domain.entity.Coffee;
import com.coffee.americanote.coffee.domain.response.CoffeeResponse;
import com.coffee.americanote.coffee.service.CoffeeService;
import com.coffee.americanote.common.entity.Degree;
import com.coffee.americanote.common.entity.ErrorCode;
import com.coffee.americanote.common.entity.Flavour;
import com.coffee.americanote.common.exception.UserException;
import com.coffee.americanote.common.validator.CommonValidator;
import com.coffee.americanote.like.repository.LikeRepository;
import com.coffee.americanote.review.domain.entity.Review;
import com.coffee.americanote.review.repository.ReviewRepository;
import com.coffee.americanote.security.jwt.util.JwtTokenProvider;
import com.coffee.americanote.user.domain.entity.User;
import com.coffee.americanote.user.domain.entity.UserFlavour;
import com.coffee.americanote.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CafeService {

    private final CafeRepository cafeRepository;
    private final CoffeeService coffeeService;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public List<CafeResponse> getAllCafe() {
        List<CafeResponse> allCafe = new ArrayList<>();
        for (Cafe cafe : cafeRepository.findAll()) {
            allCafe.add(new CafeResponse(cafe));
        }
        return allCafe;
    }

    public Set<CafeResponse> searchCafeByFiltering(SearchCafeRequest request) {
        List<Coffee> coffees = coffeeService.findAllBySearchOptions(request);

        Set<CafeResponse> result = new HashSet<>();
        for (Coffee coffee : coffees) {
            result.add(new CafeResponse(coffee.getCafe()));
        }
        return result;
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
        List<CoffeeResponse> allCoffeeData = coffeeService.getAllCoffeeData();

        // 각 커피에 대한 우선순위 부여
        HashMap<CoffeeResponse, Double> priorityMap = calculatePriorities(user, userFlavours, allCoffeeData);

        // 5명 뽑기
        List<CoffeeResponse> topCoffees = selectTopPriorityCoffees(priorityMap, 5);

        // response로 변환 후 반환
        return createRecCafeResponseList(topCoffees, user);
    }

    private HashMap<CoffeeResponse, Double> calculatePriorities(User user, List<UserFlavour> userFlavours,
                                                                 List<CoffeeResponse> allCoffeeData) {
        // 커피들의 우선순위를 저장하는 map
        HashMap<CoffeeResponse, Double> priorityMap = new HashMap<>();
        allCoffeeData.forEach(coffeeResponse -> {
            double priority = 0;
            // 여기서 향, 산미, 강도 일치하는지 확인하여 우선순위 계산
            priority += calculatePriorityForMatchingAttributes(coffeeResponse, user, userFlavours);
            priorityMap.put(coffeeResponse, priority);
        });
        return priorityMap;
    }

    private double calculatePriorityForMatchingAttributes(CoffeeResponse coffeeResponse, User user,
                                                           List<UserFlavour> userFlavours) {
        double priority = 0;
        // 향 일치 개수
        int matchingFlavours = (int) coffeeResponse.flavours().stream()
                .flatMap(flavour -> userFlavours.stream()
                        .map(UserFlavour::getFlavour)
                        .map(Flavour::getLabel)
                        .filter(userFlavourLabel -> userFlavourLabel.equals(flavour.flavour())))
                .count();
        priority += matchingFlavours;
        // 강도 일치하면 +1 / 1단계 차이나면 +0.5 / 2단계 차이나면 +0
        priority += 1 - Math.abs(
                Degree.valueOfLabel(coffeeResponse.intensity()).getWeight()
                        - user.getIntensity().getWeight());
        // 산미 일치하면 +1 / 1단계 차이나면 +0.5 / 2단계 차이나면 +0
        priority += 1 - Math.abs(
                Degree.valueOfLabel(coffeeResponse.acidity()).getWeight()
                        - user.getAcidity().getWeight());

        return priority;
    }

    private List<CoffeeResponse> selectTopPriorityCoffees(Map<CoffeeResponse, Double> priorityMap, long limitSize) {
        Comparator<Map.Entry<CoffeeResponse, Double>> randomComparator =
                Entry.<CoffeeResponse, Double>comparingByValue()
                .thenComparing(entry -> Math.random());

        return priorityMap.entrySet()
                .stream()
                .sorted(randomComparator.reversed())
                .limit(limitSize)
                .map(Map.Entry::getKey)
                .toList();
    }

    private List<CafePreviewResponse> createRecCafeResponseList(List<CoffeeResponse> topCoffees, User user) {
        List<CafePreviewResponse> result = new ArrayList<>();

        for (CoffeeResponse coffee : topCoffees) {
            // cafe
            Optional<Cafe> cafe = cafeRepository.findById(coffee.cafeId());
            CommonValidator.notNullOrThrow(cafe.orElse(null), ErrorCode.RESOURCE_NOT_FOUND.getErrorMessage());
            // reviews
            List<Review> reviews = reviewRepository.findAllByCafe(cafe.get());
            // hasLike
            boolean hasLike = likeRepository.existsByUserIdAndCafeId(user.getId(), coffee.cafeId());
            result.add(new CafePreviewResponse(cafe.get(), reviews, hasLike));
        }
        return result;
    }
}
