package com.coffee.americanote.querydsl;

import com.coffee.americanote.cafe.domain.entity.Cafe;
import com.coffee.americanote.cafe.domain.entity.QCafe;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class QuerydslTest {
    @PersistenceContext
    EntityManager em;

    @Test
    void contextLoads() {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QCafe qCafe = QCafe.cafe;
        Cafe result = query.selectFrom(qCafe)
                .where(qCafe.name.eq("브레디포스트 연남점"))
                .fetchOne();

        System.out.println("result = " + result);
    }
}
