package com.coffee.americanote.user.service;

import com.coffee.americanote.common.entity.ErrorCode;
import com.coffee.americanote.common.exception.TokenException;
import com.coffee.americanote.common.exception.UserException;
import com.coffee.americanote.common.entity.Degree;
import com.coffee.americanote.common.entity.Flavour;
import com.coffee.americanote.user.domain.entity.User;
import com.coffee.americanote.user.domain.entity.UserFlavour;
import com.coffee.americanote.user.domain.entity.UserToken;
import com.coffee.americanote.user.domain.request.UserPreferRequest;
import com.coffee.americanote.user.domain.response.UserResponse;
import com.coffee.americanote.user.repository.UserFlavourRepository;
import com.coffee.americanote.user.repository.UserRepository;
import com.coffee.americanote.user.repository.UserTokenRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyPageService {

    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final UserFlavourRepository userFlavourRepository;


    public UserResponse getMyData(String accessToken) {
        UserToken userToken = userTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new TokenException(ErrorCode.EXPIRED_TOKEN));
        User user = userRepository.findById(userToken.getUserId())
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        List<UserFlavour> flavours = new ArrayList<>(user.getFlavours());
        return new UserResponse(user, flavours);
    }

    @Transactional
    public void updatePrefer(String accessToken, UserPreferRequest userPreferRequest) {
        UserToken userToken = userTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new TokenException(ErrorCode.EXPIRED_TOKEN));
        User user = userRepository.findById(userToken.getUserId())
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
        user.updateAcidity(Degree.valueOfLabel(userPreferRequest.acidity()));
        user.updateIntensity(Degree.valueOfLabel(userPreferRequest.intensity()));
    }
}
