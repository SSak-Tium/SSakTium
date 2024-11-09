package com.sparta.ssaktium.domain.product.entity;

import com.sparta.ssaktium.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    private Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static Product createProduct(String name, BigDecimal price) {
        return new Product(name, price);
    }
}