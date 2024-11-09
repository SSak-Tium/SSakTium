package com.sparta.ssaktium.domain.orders.repository;

import com.sparta.ssaktium.domain.orders.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
