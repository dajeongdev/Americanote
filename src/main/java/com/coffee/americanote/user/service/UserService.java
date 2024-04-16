package com.coffee.americanote.user.service;

import com.coffee.americanote.common.entity.ErrorCode;
import com.coffee.americanote.common.validator.CommonValidator;
import com.coffee.americanote.security.jwt.util.JwtTokenProvider;
import com.coffee.americanote.security.service.CustomUserDetailService;
import com.coffee.americanote.user.domain.entity.User;
import com.coffee.americanote.user.domain.entity.UserToken;
import com.coffee.americanote.user.domain.request.KakaoLoginRequest;
import com.coffee.americanote.user.repository.UserRepository;
import com.coffee.americanote.user.repository.UserTokenRepository;
import com.coffee.americanote.user.repository.querydsl.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final UserQueryRepository userQueryRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoLoginService kakaoLoginService;
    private final CustomUserDetailService customUserDetailService;

    @Transactional
    public String getJwtToken(String code) {
        CommonValidator.notNullOrThrow(code, ErrorCode.INVALID_REQUEST.getErrorMessage());
        KakaoLoginRequest kakaoLoginRequest = kakaoLoginService.kakaoOAuth(code);

        Optional<UserToken> userHasToken = userQueryRepository.getUserTokenByKakaoId(kakaoLoginRequest.kakaoId());
        if (userHasToken.isPresent()) { // 유저 정보 있음
            UserToken userToken = userHasToken.get();
            // 토큰 검증 후 토큰 반환 혹은 토큰 업데이트
            return jwtTokenProvider.validateToken(userToken.getAccessToken())
                    ? userToken.getAccessToken() : updateToken(userToken);
        } else {  // 유저 정보 없음 -> 가입
            User userEntity = User.toUserEntity(kakaoLoginRequest);
            User user = userRepository.save(userEntity);
            return saveToken(user.getId());
        }
    }

    private String saveToken(Long userId) {
        Authentication authentication = getAuthentication(userId);

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

    // TODO 추후 refreshToken 사용법을 익혀서 코드 더 줄여보기!
    private String updateToken(UserToken userToken) {
        Authentication authentication = getAuthentication(userToken.getUserId());

        String accessToken = jwtTokenProvider.createAccessToken(authentication);

        // TODO save 전에 조회 쿼리를 또 한 번 날리기 때문에.. 더티체킹하고 싶은데.. 영속성아 어딨니..
        userToken.updateToken(accessToken);
        userTokenRepository.save(userToken);
        return accessToken;
    }

    // 권한 확인
    private Authentication getAuthentication(Long userId) {
        UserDetails userDetails = getUserDetails(userId);
        return createAuthentication(userDetails);
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
        CommonValidator.notNullOrThrow(accessToken, ErrorCode.EMPTY_TOKEN.getErrorMessage());
        Long userId = jwtTokenProvider.getUserId(accessToken);
        userTokenRepository.deleteByUserId(userId);
    }

    public boolean existsPreference(String accessToken) {
        CommonValidator.notNullOrThrow(accessToken, ErrorCode.EMPTY_TOKEN.getErrorMessage());
        Long userId = jwtTokenProvider.getUserId(accessToken);
        return userRepository.findById(userId).map(User::getIntensity).isPresent();
    }
}