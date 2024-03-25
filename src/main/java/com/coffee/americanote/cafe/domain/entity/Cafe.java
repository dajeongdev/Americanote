package com.coffee.americanote.cafe.domain.entity;

import com.coffee.americanote.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cafe")
@Entity
public class Cafe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafe_id")
    private Long id;

    @Column(name = "cafe_name")
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(name = "image_url")
    private String imageUrl;

    public static Cafe toCafeEntity(String name, String address,
                                    double latitude, double longitude, String imageUrl) {
        return Cafe.builder()
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .imageUrl(imageUrl)
                .build();
    }
}
