package com.sparta.ssaktium.domain.orders.controller;

import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.orders.dto.request.OrderRequestDto;
import com.sparta.ssaktium.domain.orders.entity.Order;
import com.sparta.ssaktium.domain.orders.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Tag(name = "주문 관리기능", description = "주문 관리를 할 수 있는 기능")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/v2/orders")
    @Operation(summary = "주문 등록", description = "주문 등록하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public String createOrder(@AuthenticationPrincipal
                              AuthUser authUser,
                              @RequestBody
                              OrderRequestDto orderRequestDto) {
        Order order = orderService.createOrder(authUser.getUserId(), orderRequestDto);
        return "redirect:/v2/payments-request?orderId=" + order.getId();
    }

    // 주문 실패
    @RequestMapping("/v2/orders-fail")
    public String failOrder(
            @RequestParam("orderRequestId") String orderRequestId,
            @RequestParam(value = "message", required = false, defaultValue = "orderFailed") String message,
            @RequestParam(value = "code", required = false, defaultValue = "400") Integer code

    ) {
        orderService.orderFailed(orderRequestId);
        return "redirect:/payments-fail?message=" + message + "&code=" + code;
    }
}
