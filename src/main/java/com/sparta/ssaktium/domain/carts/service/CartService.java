package com.sparta.ssaktium.domain.carts.service;

import com.sparta.ssaktium.domain.orders.dto.OrderDetailDto;
import com.sparta.ssaktium.domain.products.entity.Product;
import com.sparta.ssaktium.domain.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final ProductService productService;
    private final RedisTemplate<String, Object> redisTemplate;

    // 장바구니 추가
    public void addCart(long userId, long productId, OrderDetailDto orderDetailDto) {
        String cartKey = "cart:" + userId;

        Product product = productService.findProduct(productId);
        String productName = product.getName();

        // Redis Hash에 상품을 추가
        redisTemplate.opsForHash().put(cartKey, productName, orderDetailDto);

        // 장바구니 만료 시간 설정
        redisTemplate.expire(cartKey, 1, TimeUnit.HOURS);
    }

    // 장바구니 조회
    public Map<String, OrderDetailDto> getCart(long userId) {
        String cartKey = "cart:" + userId;

        // Redis에서 Hash 타입으로 장바구니 데이터를 조회
        Map<Object, Object> cartItems = redisTemplate.opsForHash().entries(cartKey);

        // Map<Object, Object>를 Map<String, OrderDetailDto>로 변환
        Map<String, OrderDetailDto> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : cartItems.entrySet()) {
            String productName = (String) entry.getKey();
            OrderDetailDto orderDetailDto = (OrderDetailDto) entry.getValue(); // OrderDetailDto 객체
            result.put(productName, orderDetailDto);
        }

        return result;
    }

    // 장바구니 항목 삭제
    public void removeProductFromCart(long userId, Long productId) {
        String cartKey = "cart:" + userId;
        String productIdStr = String.valueOf(productId);
        redisTemplate.opsForZSet().remove(cartKey, productIdStr);
    }
}
