package com.web.store.order.contoller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.store.order.dto.OrderResponse;
import com.web.store.order.dto.PlaceOrderRequest;
import com.web.store.order.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
        
        @PostMapping("/place")
        public String placeOrder(
                Authentication authentication,
                @RequestHeader("Authorization")
                String token,
                @RequestBody
                PlaceOrderRequest request) {

            return orderService.placeOrder(
                    token,
                    authentication.getName(),
                    request);
        }
        @GetMapping
        public List<OrderResponse>
        getMyOrders(
                Authentication authentication) {

            return orderService.getMyOrders(
                    authentication.getName());
        }
        @GetMapping("/{id}")
        public OrderResponse getOrder(
                @PathVariable("id") Long id,
                Authentication authentication) {

            return orderService.getOrder(
                    id,
                    authentication.getName());
        }
    }