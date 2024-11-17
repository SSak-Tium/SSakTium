package com.sparta.ssaktium.domain.products.Repository;

import com.sparta.ssaktium.domain.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
