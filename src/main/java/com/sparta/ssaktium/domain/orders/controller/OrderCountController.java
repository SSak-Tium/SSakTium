package com.sparta.ssaktium.domain.orders.controller;

import com.sparta.ssaktium.domain.products.entity.Product;
import com.sparta.ssaktium.domain.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class OrderCountController {

    private final ProductService productService;

    // 주문 상세페이지 이동
    @GetMapping("/v2/products/{productId}/order-count")
    public String orderDetail(@PathVariable
                              Long productId,
                              Model model) {
        Product product = productService.findProduct(productId);
        model.addAttribute("product", product);
        return "order-count";
    }
}
