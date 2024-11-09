package com.sparta.ssaktium.domain.product.Repository;

import com.sparta.ssaktium.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
