package com.coffee.americanote.user.service;

import com.coffee.americanote.common.entity.ErrorCode;
import com.coffee.americanote.common.exception.TokenException;
import com.coffee.americanote.user.domain.entity.UserToken;
import com.coffee.americanote.user.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserTokenService {

    private final UserTokenRepository userTokenRepository;

    public UserToken findTokenByAccessToken(String accessToken) {
        if (!StringUtils.hasText(accessToken)) {
            throw new TokenException(ErrorCode.EMPTY_TOKEN);
        }
        return userTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new TokenException(ErrorCode.EXPIRED_TOKEN));
    }

    @Transactional
    public void updateToken(UserToken token, String accessToken) {
        token.updateToken(accessToken);
        userTokenRepository.save(token);
    }
}
