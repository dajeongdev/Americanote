package com.coffee.americanote.user.service;

import com.coffee.americanote.common.entity.ErrorCode;
import com.coffee.americanote.common.exception.TokenException;
import com.coffee.americanote.common.exception.UserException;
import com.coffee.americanote.user.domain.entity.User;
import com.coffee.americanote.user.domain.entity.UserFlavour;
import com.coffee.americanote.user.domain.entity.UserToken;
import com.coffee.americanote.user.domain.response.UserResponse;
import com.coffee.americanote.user.repository.UserRepository;
import com.coffee.americanote.user.repository.UserTokenRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyPageService {

    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;


    public UserResponse getMyData(String accessToken) {
        UserToken userToken = userTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new TokenException(ErrorCode.EXPIRED_TOKEN));
        User user = userRepository.findById(userToken.getUserId())
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        List<UserFlavour> flavours = new ArrayList<>(user.getFlavours());
        return new UserResponse(user, flavours);
    }
}
