package com.coffee.americanote.review.domain.entity;

import com.coffee.americanote.cafe.domain.entity.Cafe;
import com.coffee.americanote.common.entity.BaseEntity;
import com.coffee.americanote.user.domain.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
@Entity
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private String content;

    @NotNull
    private Integer star;

    @Column(name = "deleted_date")
    private LocalDate deletedDate;
}
