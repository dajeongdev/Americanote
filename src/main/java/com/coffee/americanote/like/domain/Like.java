package com.coffee.americanote.like.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UserCafePK.class)
@Table(name = "user_like_cafe")
@Entity
public class Like {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "cafe_id", nullable = false)
    private Long cafeId;
}
