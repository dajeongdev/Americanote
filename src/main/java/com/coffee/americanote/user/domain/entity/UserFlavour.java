package com.coffee.americanote.user.domain.entity;

import com.coffee.americanote.global.Flavour;
import com.coffee.americanote.user.domain.request.KakaoLoginRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_flavour")
@Entity
public class UserFlavour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_flavour_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Flavour flavour;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDate createdDate;
}
