package com.coffee.americanote.like.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserCafePK implements Serializable {

    private Long userId;

    private Long cafeId;
}