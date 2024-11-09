package com.sparta.ssaktium.domain.product.service;

import com.sparta.ssaktium.domain.product.Repository.ProductRepository;
import com.sparta.ssaktium.domain.product.dto.request.ProductRequestDto;
import com.sparta.ssaktium.domain.product.dto.response.ProductResponseDto;
import com.sparta.ssaktium.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        // 상품 객체 생성
        Product product = Product.createProduct(productRequestDto.getName(), productRequestDto.getPrice());

        Product savedProduct = productRepository.save(product);

        return new ProductResponseDto(savedProduct);
    }
}
