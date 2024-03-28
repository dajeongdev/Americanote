package com.coffee.americanote.user.domain.entity;

import com.coffee.americanote.common.entity.BaseEntity;
import com.coffee.americanote.common.entity.UserRole;
import com.coffee.americanote.global.Degree;
import com.coffee.americanote.user.domain.request.KakaoLoginRequest;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "kakao_id")
    private Long kakaoId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "intensity")
    private Degree intensity;

    @Enumerated(EnumType.STRING)
    @Column(name = "acidity")
    private Degree acidity;

    @OneToMany(mappedBy = "user")
    private List<UserFlavour> flavours = new ArrayList<>();

    public static User toUserEntity(KakaoLoginRequest request) {
        return User.builder()
                .kakaoId(request.kakaoId())
                .nickname(request.nickname())
                .profileImageUrl(request.profileImageUrl())
                .role(request.role())
                .build();
    }

    public void updateIntensity(Degree intensity) {
        this.intensity = intensity;
    }

    public void updateAcidity(Degree acidity) {
        this.acidity = acidity;
    }
}