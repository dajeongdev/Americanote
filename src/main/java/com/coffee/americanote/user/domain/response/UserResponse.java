package com.coffee.americanote.user.domain.response;

import com.coffee.americanote.user.domain.entity.User;
import com.coffee.americanote.user.domain.entity.UserFlavour;
import java.util.List;
import java.util.stream.Collectors;

public record UserResponse(
        String nickname,
        String profileImageUrl,
        List<UserFlavourResponse> flavours,
        String intensity,
        String acidity

) {
    public UserResponse(User user, List<UserFlavour> flavours) {
        this(
                user.getNickname(),
                user.getProfileImageUrl(),
                flavours.stream().map(UserFlavourResponse::new)
                        .collect(Collectors.toList()),
                user.getIntensity() != null ? user.getIntensity().getLabel() : null,
                user.getAcidity() != null ? user.getAcidity().getLabel() : null
        );
    }

    public record UserFlavourResponse(
            String flavour
    ){
        public UserFlavourResponse(UserFlavour userFlavour) {
            this(userFlavour.getFlavour().getLabel());
        }
    }
}
