package com.coffee.americanote.user.service;

import com.coffee.americanote.security.service.CustomUserDetailService;
import com.coffee.americanote.security.jwt.util.JwtTokenProvider;
import com.coffee.americanote.user.domain.entity.User;
import com.coffee.americanote.user.domain.entity.UserToken;
import com.coffee.americanote.user.domain.request.KakaoLoginRequest;
import com.coffee.americanote.user.repository.UserRepository;
import com.coffee.americanote.user.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final KakaoLoginService kakaoLoginService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;

    @Transactional
    public String getJwtToken(String code) {
        // 토큰 조회 후 사용자 정보 조회
        KakaoLoginRequest kakaoLoginRequest = kakaoLoginService.kakaoOAuth(code);

        // 조회해 온 정보로 회원 조회 및 가입
        User userEntity = User.toUserEntity(kakaoLoginRequest);
        User user = userRepository.findByKakaoId(kakaoLoginRequest.kakaoId())
                .orElseGet(() -> userRepository.save(userEntity));

        // 토큰 생성 및 매핑
        return getAccessToken(user);
    }

    private String getAccessToken(User user) {
        Long userId = user.getId();
        UserDetails userDetails = getUserDetails(userId);
        Authentication authentication = createAuthentication(userDetails);

        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        UserToken userToken = UserToken.builder()
                .userId(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        userTokenRepository.save(userToken);
        return accessToken;
    }

    private UserDetails getUserDetails(Long userId) {
        return customUserDetailService.loadUserByUsername(String.valueOf(userId));
    }

    private Authentication createAuthentication(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
    }

    @Transactional
    public void logout(String accessToken) {
        Long userId = jwtTokenProvider.getUserId(accessToken);
        userTokenRepository.deleteByUserId(userId);
    }

    public boolean existsPreference(String accessToken) {
        Long userId = jwtTokenProvider.getUserId(accessToken);
        return userRepository.findById(userId).map(User::getIntensity).isPresent();
    }
}