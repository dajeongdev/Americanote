package com.coffee.americanote.user.domain.entity;

import com.coffee.americanote.common.entity.BaseEntity;
import com.coffee.americanote.common.entity.UserRole;
import com.coffee.americanote.user.domain.request.KakaoLoginRequest;
import jakarta.persistence.*;
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

    public static User toUserEntity(KakaoLoginRequest request) {
        return User.builder()
                .kakaoId(request.kakaoId())
                .nickname(request.nickname())
                .profileImageUrl(request.profileImageUrl())
                .role(request.role())
                .build();
    }
}