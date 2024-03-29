package com.coffee.americanote.mypage.service;

import com.coffee.americanote.cafe.domain.response.CafeSearchResponse;
import com.coffee.americanote.cafe.repository.querydsl.CafeQueryRepository;
import com.coffee.americanote.common.entity.Degree;
import com.coffee.americanote.common.entity.ErrorCode;
import com.coffee.americanote.common.entity.Flavour;
import com.coffee.americanote.common.exception.UserException;
import com.coffee.americanote.common.validator.CommonValidator;
import com.coffee.americanote.security.jwt.util.JwtTokenProvider;
import com.coffee.americanote.user.domain.entity.User;
import com.coffee.americanote.user.domain.entity.UserFlavour;
import com.coffee.americanote.user.domain.request.UserPreferRequest;
import com.coffee.americanote.user.domain.response.UserResponse;
import com.coffee.americanote.user.repository.UserFlavourRepository;
import com.coffee.americanote.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyPageService {

    private final UserRepository userRepository;
    private final UserFlavourRepository userFlavourRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CafeQueryRepository cafeQueryRepository;

    public UserResponse getMyData(String accessToken) {
        CommonValidator.notNullOrThrow(accessToken, "AccessToken이 없습니다.");
        Long userId = jwtTokenProvider.getUserId(accessToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        List<UserFlavour> flavours = new ArrayList<>(user.getFlavours());
        return new UserResponse(user, flavours);
    }

    @Transactional
    public void updatePrefer(String accessToken, UserPreferRequest userPreferRequest) {
        CommonValidator.notNullOrThrow(accessToken, "AccessToken이 없습니다.");
        Long userId = jwtTokenProvider.getUserId(accessToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        List<UserFlavour> existingUserFlavours = userFlavourRepository.findAllByUser(user);

        List<Flavour> preferFlavours = userPreferRequest.flavours()
                .stream().map(Flavour::valueOfLabel).toList();

        Map<Flavour, UserFlavour> existingFlavourMap = Optional.ofNullable(existingUserFlavours)
                .map(flavours -> flavours.stream()
                        .collect(Collectors.toMap(UserFlavour::getFlavour, f -> f)))
                .orElse(Collections.emptyMap());

        // 새로운 향들 처리
        for (Flavour prefer : preferFlavours) {
            if (existingFlavourMap.containsKey(prefer)) {
                // 이미 존재하는 경우
                // 삭제할 후보에서 제거
                existingFlavourMap.remove(prefer);
            } else {
                // 존재하지 않는 경우
                UserFlavour newFlavour = UserFlavour.builder().user(user).flavour(prefer).build();
                userFlavourRepository.save(newFlavour);
            }
        }

        // 삭제할 후보로 남은 향들 삭제
        userFlavourRepository.deleteAll(existingFlavourMap.values());

        // 강도, 산미 update
        user.updateAcidity(userPreferRequest.acidity() != null ? Degree.valueOfLabel(userPreferRequest.acidity()) : null);
        user.updateIntensity(userPreferRequest.intensity() != null ? Degree.valueOfLabel(userPreferRequest.intensity()) : null);
    }

    public List<CafeSearchResponse> getAllUserLikeCafe(String accessToken) {
        CommonValidator.notNullOrThrow(accessToken, "AccessToken이 없습니다.");
        Long userId = jwtTokenProvider.getUserId(accessToken);
        return cafeQueryRepository.getAllUserLikeCafe(userId);
    }
}
