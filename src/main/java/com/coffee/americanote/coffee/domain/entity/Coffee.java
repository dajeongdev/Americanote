package com.coffee.americanote.coffee.domain.entity;

import com.coffee.americanote.cafe.domain.entity.Cafe;
import com.coffee.americanote.common.entity.BaseEntity;
import com.coffee.americanote.global.Flavour;
import com.coffee.americanote.global.Degree;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "coffee")
@Entity
public class Coffee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coffee_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @Column(name = "coffee_name", nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Flavour flavour;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Degree intensity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Degree acidity;

    @Column(nullable = false)
    private int price;

    public static Coffee toCoffeeEntity(Cafe cafe, String name, String flavour,
                                 String intensity, String acidity, int price) {
        return Coffee.builder()
                .cafe(cafe)
                .name(name)
                .flavour(Flavour.valueOfLabel(flavour))
                .intensity(Degree.valueOfLabel(intensity))
                .acidity(Degree.valueOfLabel(acidity))
                .price(price)
                .build();
    }
}
