package com.sparta.ssaktium.domain.products.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.products.dto.request.ProductRequestDto;
import com.sparta.ssaktium.domain.products.dto.response.ProductResponseDto;
import com.sparta.ssaktium.domain.products.entity.Product;
import com.sparta.ssaktium.domain.products.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Tag(name = "상품 관리기능", description = "상품 관리를 할 수 있는 기능")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/ssaktium/shopping")
    public String getShoppingPage(Model model) {
        // DB에서 상품 리스트 조회
        model.addAttribute("products", productService.findAllProduct());
        return "shopping"; // shopping.html 페이지로 이동
    }

    @GetMapping("/ssaktium/order/{productId}")
    public String getOrderPage(@PathVariable Long productId, Model model) {
        Product product = productService.findProduct(productId);
        model.addAttribute("product", product);
        return "order-place";
    }

    @Secured("ROLE_ADMIN")
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
