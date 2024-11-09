package com.sparta.ssaktium.domain.orders.service;

import com.sparta.ssaktium.domain.orders.dto.request.OrderRequestDto;
import com.sparta.ssaktium.domain.orders.dto.response.OrderResponseDto;
import com.sparta.ssaktium.domain.orders.entity.Order;
import com.sparta.ssaktium.domain.orders.repository.OrderRepository;
import com.sparta.ssaktium.domain.products.dto.request.ProductRequestDto;
import com.sparta.ssaktium.domain.products.dto.response.ProductResponseDto;
import com.sparta.ssaktium.domain.products.entity.Product;
import com.sparta.ssaktium.domain.products.service.ProductService;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;

    @Transactional
    public Order createOrder(long productId, OrderRequestDto orderRequestDto) {
//        User user = userService.findUser(userId);
        Product product = productService.findProduct(productId);
        String customerKey = "customerKey" + getRandomNumber(8);


        Order order = Order.createOrder(product.getPrice(), UUID.randomUUID().toString(), customerKey, orderRequestDto.getRecipient(), orderRequestDto.getAddress());

        return orderRepository.save(order);
    }

    // 랜덤 숫자 8자 문자열 추가
    private String getRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // 0부터 9까지의 랜덤 숫자 추가
        }
        return sb.toString();
    }
}
