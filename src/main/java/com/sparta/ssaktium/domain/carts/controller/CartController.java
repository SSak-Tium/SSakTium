package com.sparta.ssaktium.domain.carts.controller;

import com.sparta.ssaktium.domain.carts.service.CartService;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.orders.dto.OrderDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/ssaktium/cart")
    public String cart() {
        return "cart";
    }

    // 장바구니에 추가
    @PostMapping("/v2/products/{productId}/add-cart")
    public String addCart(@AuthenticationPrincipal
                          AuthUser authUser,
                          @PathVariable
                          long productId,
                          @RequestBody
                          OrderDetailDto orderDetailDto) {
        cartService.addCart(authUser.getUserId(), productId, orderDetailDto);
        return "redirect:/ssaktium/main";
    }

    // 장바구니 조회
    @GetMapping("/ssaktium/carts")
    public String getCart(@AuthenticationPrincipal AuthUser authUser, Model model) {
        Map<String, OrderDetailDto> cartItems = cartService.getCart(authUser.getUserId());

        model.addAttribute("userName", authUser.getUserName());
        model.addAttribute("cartItems", cartItems);

        return "cart";
    }

    // 장바구니 물건 삭제
    @DeleteMapping("/v2/carts")
    public void removeProductFromCart(@AuthenticationPrincipal
                                      AuthUser authUser,
                                      @RequestParam
                                      Long productId) {
        cartService.removeProductFromCart(authUser.getUserId(), productId);
    }
}
