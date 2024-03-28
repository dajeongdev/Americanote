package com.coffee.americanote.coffee.repository;

import static com.coffee.americanote.coffee.domain.entity.QCoffeeFlavour.*;

import com.coffee.americanote.cafe.domain.request.SearchCafeRequest;
import com.coffee.americanote.coffee.domain.entity.CoffeeFlavour;
import com.coffee.americanote.common.entity.Flavour;
import com.coffee.americanote.common.exception.CommonException;
import com.coffee.americanote.common.exception.CommonExceptionHandler;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.http.HttpStatus;

public class CoffeeFlavourRepositoryImpl implements CoffeeFlavourRepositoryCustom {

    private final JPAQueryFactory query;

    public CoffeeFlavourRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<CoffeeFlavour> findInFlavours(SearchCafeRequest request) {
        JPQLQuery<CoffeeFlavour> jpqlQuery = query.selectFrom(coffeeFlavour)
                .where(inFlavour(request.flavours()));

        return jpqlQuery.fetch();
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
}
