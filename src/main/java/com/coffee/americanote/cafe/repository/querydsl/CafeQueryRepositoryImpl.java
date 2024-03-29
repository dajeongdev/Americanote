package com.coffee.americanote.cafe.repository.querydsl;

import com.coffee.americanote.cafe.domain.entity.QCafe;
import com.coffee.americanote.cafe.domain.response.CafeSearchResponse;
import com.coffee.americanote.coffee.domain.entity.QCoffee;
import com.coffee.americanote.coffee.domain.entity.QCoffeeFlavour;
import com.coffee.americanote.like.domain.QLike;
import com.coffee.americanote.review.domain.entity.QReview;
import com.coffee.americanote.user.domain.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
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
}