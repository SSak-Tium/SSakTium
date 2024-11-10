package com.sparta.ssaktium.domain.payments.repository;

import com.sparta.ssaktium.domain.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
