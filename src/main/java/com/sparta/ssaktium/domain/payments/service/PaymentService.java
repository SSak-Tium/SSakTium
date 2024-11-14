package com.sparta.ssaktium.domain.payments.service;

import com.sparta.ssaktium.domain.carts.service.CartService;
import com.sparta.ssaktium.domain.orders.entity.Order;
import com.sparta.ssaktium.domain.orders.service.OrderService;
import com.sparta.ssaktium.domain.payments.entity.Payment;
import com.sparta.ssaktium.domain.payments.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final OrderService orderService;
    private final CartService cartService;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void createPayment(JSONObject jsonObject) {

        // 주문 객체 가져오기
        Order order = orderService.findByOrderRequestId(jsonObject.get("orderId") != null ? jsonObject.get("orderId").toString() : null);

        // totalAmount를 BigDecimal로 변환 (null 체크 추가)
        Object totalAmountObj = jsonObject.get("totalAmount");
        BigDecimal amount = new BigDecimal(totalAmountObj.toString());

        // requestedAt과 approvedAt을 LocalDateTime으로 변환
        LocalDateTime requestedAt = OffsetDateTime.parse(jsonObject.get("requestedAt").toString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDateTime();
        LocalDateTime approvedAt = OffsetDateTime.parse(jsonObject.get("approvedAt").toString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDateTime();

        // Payment 객체 생성
        Payment payment = Payment.createPayment(
                order,
                amount,
                jsonObject.get("method") != null ? jsonObject.get("method").toString() : "UNKNOWN", // default value for method
                jsonObject.get("paymentKey") != null ? jsonObject.get("paymentKey").toString() : "UNKNOWN", // default value for paymentKey
                jsonObject.get("orderId").toString(),
                requestedAt,
                approvedAt
        );

        // 장바구니 전체 삭제
        cartService.clearCart(order.getUser().getId());

        // 결제 정보 저장
        paymentRepository.save(payment);
    }

}