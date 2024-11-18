package com.sparta.ssaktium.domain.products.service;

import com.sparta.ssaktium.domain.products.Repository.ProductRepository;
import com.sparta.ssaktium.domain.products.dto.request.ProductRequestDto;
import com.sparta.ssaktium.domain.products.dto.response.ProductResponseDto;
import com.sparta.ssaktium.domain.products.entity.Product;
import com.sparta.ssaktium.domain.products.exception.NotFountProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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


    public Product findProduct(long productId) {
        return productRepository.findById(productId).orElseThrow(NotFountProductException::new);
    }

    public List<ProductResponseDto> findAllProduct() {
        // Product 엔티티 목록을 가져온 후 각 엔티티를 ProductResponseDto로 변환
        return productRepository.findAll().stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
}
