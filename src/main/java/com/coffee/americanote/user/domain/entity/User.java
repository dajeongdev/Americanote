package com.coffee.americanote.user.domain.entity;

import com.coffee.americanote.common.entity.BaseEntity;
import com.coffee.americanote.user.domain.request.KakaoLoginRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kakao_id")
    private Long kakaoId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    public static User toUserEntity(KakaoLoginRequest request) {
        return User.builder()
                .kakaoId(request.kakaoId())
                .nickname(request.nickname())
                .profileImageUrl(request.profileImageUrl())
                .build();
    }
}