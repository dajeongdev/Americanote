package com.coffee.americanote.cafe.repository.querydsl;

import com.coffee.americanote.cafe.domain.entity.QCafe;
import com.coffee.americanote.cafe.domain.request.SearchCafeRequest;
import com.coffee.americanote.cafe.domain.response.CafeResponse;
import com.coffee.americanote.cafe.domain.response.CafeSearchResponse;
import com.coffee.americanote.coffee.domain.entity.QCoffee;
import com.coffee.americanote.coffee.domain.entity.QCoffeeFlavour;
import com.coffee.americanote.common.entity.Degree;
import com.coffee.americanote.common.entity.Flavour;
import com.coffee.americanote.common.exception.CommonException;
import com.coffee.americanote.like.domain.QLike;
import com.coffee.americanote.review.domain.entity.QReview;
import com.coffee.americanote.user.domain.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQueryFactory;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CafeQueryRepositoryImpl implements CafeQueryRepository {

    private final JPQLQueryFactory queryFactory;

    private final QUser user = QUser.user;
    private final QCafe cafe = QCafe.cafe;
    private final QLike like = QLike.like;
    private final QCoffee coffee = QCoffee.coffee;
    private final QCoffeeFlavour coffeeFlavour = QCoffeeFlavour.coffeeFlavour;
    private final QReview review = QReview.review;

    @Override
    public List<CafeSearchResponse> getAllUserLikeCafe(Long userId) {
        return queryFactory.select(Projections.constructor(CafeSearchResponse.class,
                        cafe.id, cafe.name, cafe.imageUrl,
                        coffee.intensity, coffee.acidity,
                        Expressions.stringTemplate("group_concat({0})", coffeeFlavour.flavour).as("flavours"),
                        MathExpressions.round(review.star.avg(), 1).as("avgStar"),
                        JPAExpressions.selectFrom(like)
                                .where(like.userId.eq(userId).and(like.cafeId.eq(cafe.id)))
                                .exists().as("hasLike")
                ))
                .from(user)
                .leftJoin(like).on(like.userId.eq(user.id))
                .innerJoin(cafe).on(cafe.id.eq(like.cafeId))
                .innerJoin(review).on(review.cafe.id.eq(cafe.id))
                .innerJoin(coffee).on(coffee.cafe.id.eq(cafe.id))
                .innerJoin(coffeeFlavour).on(coffeeFlavour.coffee.id.eq(coffee.id))
                .where(user.id.eq(userId))
                .groupBy(cafe.id)
                .stream()
                .toList();
    }

    @Override
    public List<CafeSearchResponse> getAllSearchCafe(String keyword, Long userId) {
        return queryFactory.select(Projections.constructor(CafeSearchResponse.class,
                        cafe.id, cafe.name, cafe.imageUrl,
                        coffee.intensity, coffee.acidity,
                        Expressions.stringTemplate("group_concat({0})", coffeeFlavour.flavour).as("flavours"),
                        MathExpressions.round(review.star.avg(), 1).as("avgStar"),
                        JPAExpressions.selectFrom(like)
                                .where(like.userId.eq(userId).and(like.cafeId.eq(cafe.id)))
                                .exists().as("hasLike")
                ))
                .from(cafe)
                .innerJoin(review).on(review.cafe.id.eq(cafe.id))
                .innerJoin(coffee).on(coffee.cafe.id.eq(cafe.id))
                .innerJoin(coffeeFlavour).on(coffeeFlavour.coffee.id.eq(coffee.id))
                .where(cafe.name.contains(keyword))
                .groupBy(cafe.id)
                .stream()
                .toList();
    }

    @Override
    public Set<CafeResponse> getAllFilteringCafe(SearchCafeRequest request) {
        return queryFactory.select(Projections.constructor(CafeResponse.class,
                cafe.id, cafe.latitude, cafe.longitude
                ))
                .from(cafe)
                .innerJoin(coffee).on(coffee.cafe.id.eq(cafe.id))
                .innerJoin(coffeeFlavour).on(coffeeFlavour.coffee.id.eq(coffee.id))
                .where(inFlavour(request.flavours()),
                        priceRange(request.priceRanges()),
                        inIntensity(request.intensities()),
                        inAcidity(request.acidities()))
                .groupBy(cafe.id)
                .stream().collect(Collectors.toSet());
    }

    private BooleanExpression inFlavour(List<String> flavours) {
        if (flavours == null || flavours.isEmpty()) {
            return coffeeFlavour.flavour.isNotNull();
        }
        if (flavours.size() > 3) {
            throw new CommonException("허용된 개수 초과", HttpStatus.BAD_REQUEST);
        }
        List<Flavour> flavourEnums = flavours.stream()
                .map(Flavour::valueOfLabel).toList();
        return coffeeFlavour.flavour.in(flavourEnums);
    }

    private BooleanExpression priceRange(List<String> ranges) {
        if (ranges == null || ranges.isEmpty() || ranges.size() == 3) {
            return coffee.price.isNotNull();
        }
        BooleanExpression expression = null;

        for (String range : ranges) {
            switch (range) {
                case "<" -> expression = (expression == null) ?
                        coffee.price.lt(5000) : expression.or(coffee.price.lt(5000));
                case "=" -> expression = (expression == null) ?
                        coffee.price.eq(5000) : expression.or(coffee.price.eq(5000));
                case ">" -> expression = (expression == null) ?
                        coffee.price.gt(5000) : expression.or(coffee.price.gt(5000));
                default -> throw new CommonException("존재하지 않는 범위", HttpStatus.NOT_FOUND);
            }
        }
        return expression != null ? expression : coffee.price.isNotNull();
    }

    private BooleanExpression inIntensity(List<String> intensities) {
        if (intensities == null || intensities.isEmpty() || intensities.size() == 3) {
            return coffee.intensity.isNotNull();
        }
        List<Degree> intensityEnums = intensities.stream()
                .map(Degree::valueOfLabel).toList();
        return coffee.intensity.in(intensityEnums);
    }

    private BooleanExpression inAcidity(List<String> acidities) {
        if (acidities == null || acidities.isEmpty() || acidities.size() == 3) {
            return coffee.acidity.isNotNull();
        }
        List<Degree> acidityEnums = acidities.stream()
                .map(Degree::valueOfLabel).toList();
        return coffee.acidity.in(acidityEnums);
    }
}