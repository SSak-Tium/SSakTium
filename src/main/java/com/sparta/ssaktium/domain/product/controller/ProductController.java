package com.sparta.ssaktium.domain.product.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.product.dto.request.ProductRequestDto;
import com.sparta.ssaktium.domain.product.dto.response.ProductResponseDto;
import com.sparta.ssaktium.domain.product.entity.Product;
import com.sparta.ssaktium.domain.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "상품 관리기능", description = "상품 관리를 할 수 있는 기능")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/v2/products")
    @Operation(summary = "상품 등록", description = "상품 등록하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<ProductResponseDto>> createProduct(@Valid
                                                                 @RequestBody
                                                                 @Parameter(description = "상품정보 입력")
                                                                 ProductRequestDto productRequestDto) {
        return ResponseEntity.ok(CommonResponse.success(productService.createProduct(productRequestDto)));

    }
}
