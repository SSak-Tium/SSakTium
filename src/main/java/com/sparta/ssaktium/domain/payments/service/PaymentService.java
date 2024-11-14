package com.sparta.ssaktium.domain.payments.service;

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
    private final PaymentRepository paymentRepository;

    @Transactional
    public void createPayment(JSONObject jsonObject) {

        // 주문 객체 가져오기
        Order order = orderService.findByOrderRequestId(jsonObject.get("orderId") != null ? jsonObject.get("orderId").toString() : null);
        if (order == null) {
            throw new IllegalArgumentException("Order not found for the given orderRequestId: " + jsonObject.get("orderId"));
        }

        // totalAmount를 BigDecimal로 변환 (null 체크 추가)
        Object totalAmountObj = jsonObject.get("totalAmount");
        if (totalAmountObj == null) {
            throw new IllegalArgumentException("totalAmount is missing in the request");
        }
        BigDecimal amount = new BigDecimal(totalAmountObj.toString());

        // requestedAt과 approvedAt을 LocalDateTime으로 변환 (null 체크 추가)
        LocalDateTime requestedAt = parseDate(jsonObject.get("requestedAt"));
        LocalDateTime approvedAt = parseDate(jsonObject.get("approvedAt"));

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

        // 결제 정보 저장
        paymentRepository.save(payment);
    }

    // 날짜 변환을 위한 별도 메서드
    private LocalDateTime parseDate(Object dateObj) {
        if (dateObj == null) {
            throw new IllegalArgumentException("Date is missing in the request");
        }
        try {
            return OffsetDateTime.parse(dateObj.toString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDateTime();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format: " + dateObj.toString());
        }
    }
}