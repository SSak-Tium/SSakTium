package com.sparta.ssaktium.domain.carts.service;

import com.sparta.ssaktium.domain.orders.dto.OrderCountDto;
import com.sparta.ssaktium.domain.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final ProductService productService;
    private final RedisTemplate<String, Object> redisTemplate;

    // 장바구니 추가
    public void addCart(long userId, long productId, OrderCountDto orderCountDto) {
        String cartKey = "cart:" + userId;

        // Redis Hash에 상품을 추가
        redisTemplate.opsForHash().put(cartKey, String.valueOf(productId), orderCountDto);

        // 장바구니 만료 시간 설정
        redisTemplate.expire(cartKey, 1, TimeUnit.HOURS);
    }

    // 장바구니 조회
    public Map<String, OrderCountDto> getCart(long userId) {
        String cartKey = "cart:" + userId;

        // Redis에서 Hash 타입으로 장바구니 데이터를 조회
        Map<Object, Object> cartItems = redisTemplate.opsForHash().entries(cartKey);

        // Map<Object, Object>를 Map<String, OrderDetailDto>로 변환
        Map<String, OrderCountDto> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : cartItems.entrySet()) {
            String productName = (String) entry.getKey();
            OrderCountDto orderCountDto = (OrderCountDto) entry.getValue(); // OrderDetailDto 객체
            result.put(productName, orderCountDto);
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
