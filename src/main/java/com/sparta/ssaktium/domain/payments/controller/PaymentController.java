package com.sparta.ssaktium.domain.payments.controller;

import com.sparta.ssaktium.domain.orders.entity.Order;
import com.sparta.ssaktium.domain.orders.service.OrderService;
import com.sparta.ssaktium.domain.payments.entity.Payment;
import com.sparta.ssaktium.domain.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final OrderService orderService;
    private PaymentService paymentService;

    @Value("${toss.secret-key}")
    private String widgetSecretKey;

    // 결제 요청
    @GetMapping("/v2/payments-request")
    public String paymentsRequest(
            @RequestParam("orderId") Long orderId,
            Model model
    ) {
        Order order = orderService.findOrder(orderId);

        // 모델에 필요한 속성 추가
        model.addAttribute("customerName", order.getRecipient());
        model.addAttribute("customerAddress", order.getAddress());
        model.addAttribute("orderRequestId", order.getOrderRequestId());
        model.addAttribute("totalPrice", order.getTotalPrice());
        model.addAttribute("customerKey", order.getCustomerKey());

        return "payment-request";   // payment-request.html 템플릿 렌더링
    }

    // 주문 성공 결제 승인 요청 (주문 요청됨)
    @GetMapping("/v2/payments-success")
    public String paymentsSuccess(
            @RequestParam("orderRequestId") String orderRequestId
    ) {
        orderService.orderRequested(orderRequestId);
        return "payment-success";
    }

    // 결제 실패
    @GetMapping("/v2/payments-fail")
    public String paymentsFail(
            @RequestParam("message") String message,
            @RequestParam("code") Integer code,
            Model model
    ) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "payment-fail";
    }
}
