package com.sparta.ssaktium.domain.orders.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private long id;
    private BigDecimal totalPrice;
    private String orderRequestId;
    private String customerKey;
    private String recipient;
    private String address;


}
