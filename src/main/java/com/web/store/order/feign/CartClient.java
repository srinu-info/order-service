package com.web.store.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.web.store.order.dto.CartResponse;

@FeignClient(
        name = "cart-service",
        url = "http://localhost:8083")
public interface CartClient {

    @GetMapping("/cart")
    CartResponse getCart(
            @RequestHeader("Authorization")
            String token);

    @DeleteMapping("/cart/clear")
    String clearCart(
            @RequestHeader("Authorization")
            String token);
}