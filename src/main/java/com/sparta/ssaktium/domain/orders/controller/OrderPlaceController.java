package com.sparta.ssaktium.domain.orders.controller;

import com.sparta.ssaktium.domain.orders.dto.OrderPriceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class OrderPlaceController {

    // 주문 배송지입력페이지 이동
    @PostMapping("/ssaktium/order-place")
    public String orderPlace(@RequestBody
                              OrderPriceDto orderPriceDto,
                              Model model) {
        int totalPrice = orderPriceDto.getTotalPrice();
        model.addAttribute("totalPrice", totalPrice);
        return "order-place";
    }
}
