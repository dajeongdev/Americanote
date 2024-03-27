package com.coffee.americanote.user.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_token")
@Entity
public class UserToken {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    public void updateToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
