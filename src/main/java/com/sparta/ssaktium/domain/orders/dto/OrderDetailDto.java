package com.sparta.ssaktium.domain.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto {

    private int price;
    private int amount;
}
