package com.coffee.americanote.coffee.repository;

import static com.coffee.americanote.coffee.domain.entity.QCoffee.*;

import com.coffee.americanote.cafe.domain.request.SearchCafeRequest;
import com.coffee.americanote.coffee.domain.entity.Coffee;
import com.coffee.americanote.common.entity.Degree;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;

public class CoffeeRepositoryImpl implements CoffeeRepositoryCustom{

    private final JPAQueryFactory query;

    public CoffeeRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<Coffee> findBySearchOption(SearchCafeRequest request) {
        JPQLQuery<Coffee> jpqlQuery = query.selectFrom(coffee)
                .where(priceRange(request.priceRange()), inIntensity(request.intensities()),
                        inAcidity(request.acidities()));

        return jpqlQuery.fetch();
    }

    private BooleanExpression priceRange(String range) {
        if (range == null) return coffee.price.isNotNull();
        return switch (range) {
            case "<" -> coffee.price.lt(5000);
            case "=" -> coffee.price.eq(5000);
            case ">" -> coffee.price.gt(5000);
            default -> coffee.price.isNotNull();
        };
    }

    private BooleanExpression inIntensity(List<String> intensities) {
        if (intensities == null || intensities.isEmpty()) {
            return coffee.intensity.isNotNull();
        }
        List<Degree> intensityEnums = intensities.stream()
                .map(Degree::valueOfLabel).toList();
        return coffee.intensity.in(intensityEnums);
    }

    private BooleanExpression inAcidity(List<String> acidities) {
        if (acidities == null || acidities.isEmpty()) {
            return coffee.acidity.isNotNull();
        }
        List<Degree> acidityEnums = acidities.stream()
                .map(Degree::valueOfLabel).toList();
        return coffee.acidity.in(acidityEnums);
    }
}
