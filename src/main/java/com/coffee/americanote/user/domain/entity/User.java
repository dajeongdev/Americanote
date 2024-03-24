package com.coffee.americanote.user.domain.entity;

import com.coffee.americanote.common.BaseEntity;
import com.coffee.americanote.user.domain.request.UserRequest;
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

    @Column
    private String name;

    public static User toUserEntity(UserRequest userRequest) {
        return User.builder()
                .name(userRequest.name())
                .build();
    }
}