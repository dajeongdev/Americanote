package com.coffee.americanote.user.service;

import com.coffee.americanote.user.domain.entity.User;
import com.coffee.americanote.user.domain.entity.UserToken;
import com.coffee.americanote.user.domain.request.KakaoLoginRequest;
import com.coffee.americanote.user.oauth2.KakaoLoginUtil;
import com.coffee.americanote.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final KakaoLoginUtil kakaoLoginUtil;

    public UserToken getJwtToken(String code) {
        // 인가 코드 받아서 토큰 조회 후 사용자 정보 조회 (임시)
        KakaoLoginRequest kakaoLoginRequest = kakaoLoginUtil.kakaoOAuth(code);

        // TODO 조회해 온 정보로 회원 조회 및 가입
        User userEntity = User.toUserEntity(kakaoLoginRequest);
        User user = userRepository.findByKakaoId(kakaoLoginRequest.kakaoId())
                .orElseGet(() -> userRepository.save(userEntity));
        log.info("userId = {}", user.getId());

        // TODO 토큰 생성
        Long userId = user.getId();

        // TODO 토큰 매핑
        return UserToken.builder()
                .accessToken(null)
                .refreshToken(null)
                .build();
    }
}