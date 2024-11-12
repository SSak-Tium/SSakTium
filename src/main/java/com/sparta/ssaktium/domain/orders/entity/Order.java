package com.sparta.ssaktium.domain.orders.entity;

import com.sparta.ssaktium.domain.common.entity.Timestamped;
import com.sparta.ssaktium.domain.orders.enums.OrderStatus;
import com.sparta.ssaktium.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String orderName;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private String orderRequestId;

    @Column(nullable = false)
    private String customerKey;

    //    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    private Order(String orderName, BigDecimal totalPrice, String orderRequestId, String customerKey, String recipient, String address) {
        this.orderName = orderName;
        this.totalPrice = totalPrice;
        this.orderRequestId = orderRequestId;
        this.customerKey = customerKey;
        this.recipient = recipient;
        this.address = address;
    }

    public static Order createOrder(String orderName, BigDecimal totalPrice, String orderRequestId, String customerKey, String recipient, String address) {
        return new Order(orderName, totalPrice, orderRequestId, customerKey, recipient, address);
    }

    // 주문 상태 변경
    public void updateStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }
}
