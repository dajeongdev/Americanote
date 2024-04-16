package com.coffee.americanote.user.repository.querydsl;

import com.coffee.americanote.user.domain.entity.QUser;
import com.coffee.americanote.user.domain.entity.QUserToken;
import com.coffee.americanote.user.domain.entity.UserToken;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserQueryRepositoryImpl implements UserQueryRepository {

    private final JPQLQueryFactory queryFactory;

    private final QUser user = QUser.user;
    private final QUserToken userToken = QUserToken.userToken;

    @Override
    public Optional<UserToken> getUserTokenByKakaoId(Long kakaoId) {
        return Optional.ofNullable(
                queryFactory.select(Projections.constructor(UserToken.class,
                        userToken.id, userToken.userId, userToken.accessToken, userToken.refreshToken
                ))
                .from(user)
                .leftJoin(userToken).on(user.id.eq(userToken.userId))
                .where(user.kakaoId.eq(kakaoId))
                .fetchOne()
        );
    }
}