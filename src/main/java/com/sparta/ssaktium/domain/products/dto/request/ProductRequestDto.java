package com.sparta.ssaktium.domain.products.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    @NotBlank
    @Schema(description = "상품명", example = "영양제")
    private String name;

    @NotNull
    @Schema(description = "가격", example = "3000")
    private int price;
}
