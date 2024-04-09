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

@Slf4j
@RequiredArgsConstructor
@Service
public class MyPageService {

    private final UserRepository userRepository;
    private final UserFlavourRepository userFlavourRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CafeQueryRepository cafeQueryRepository;

    public UserResponse getMyData(String accessToken) {
        Long userId = checkAccessToken(accessToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        List<UserFlavour> flavours = new ArrayList<>(user.getFlavours());
        return new UserResponse(user, flavours);
    }

    @Transactional
    public void updatePrefer(String accessToken, UserPreferRequest userPreferRequest) {
        Long userId = checkAccessToken(accessToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        List<UserFlavour> originalUserFlavours = userFlavourRepository.findAllByUser(user);
        List<Flavour> requestUserFlavours = userPreferRequest.flavours()
                .stream().map(Flavour::valueOfLabel).toList();

        List<UserFlavour> newUserFlavours = getNewUserFlavoursList(user, originalUserFlavours, requestUserFlavours);
        userFlavourRepository.saveAll(newUserFlavours);
        userFlavourRepository.deleteAll(originalUserFlavours);

        user.updateAcidity(userPreferRequest.acidity() != null ? Degree.valueOfLabel(userPreferRequest.acidity()) : null);
        user.updateIntensity(userPreferRequest.intensity() != null ? Degree.valueOfLabel(userPreferRequest.intensity()) : null);
    }

    private static List<UserFlavour> getNewUserFlavoursList(User user, List<UserFlavour> originalUserFlavours,
                                                               List<Flavour> requestUserFlavours) {
        List<UserFlavour> newUserFlavours = new ArrayList<>();
        for (Flavour prefer : requestUserFlavours) {
            boolean found = false;
            for (UserFlavour originalFlavour : originalUserFlavours) {
                if (originalFlavour.getFlavour().equals(prefer)) {
                    originalUserFlavours.remove(originalFlavour);
                    found = true;
                    break;
                }
            }
            if (!found) {
                newUserFlavours.add(UserFlavour.builder()
                        .user(user)
                        .flavour(prefer)
                        .build());
            }
        }
        return newUserFlavours;
    }

    public List<CafeSearchResponse> getAllUserLikeCafe(String accessToken) {
        Long userId = checkAccessToken(accessToken);
        return cafeQueryRepository.getAllUserLikeCafe(userId);
    }

    private Long checkAccessToken(String accessToken) {
        CommonValidator.notNullOrThrow(accessToken, ErrorCode.EMPTY_TOKEN.getErrorMessage());
        return jwtTokenProvider.getUserId(accessToken);
    }
}
