package com.web.store.order.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.store.order.dto.CartItemResponse;
import com.web.store.order.dto.CartResponse;
import com.web.store.order.dto.OrderItemResponse;
import com.web.store.order.dto.OrderResponse;
import com.web.store.order.dto.PlaceOrderRequest;
import com.web.store.order.entity.Order;
import com.web.store.order.entity.OrderItem;
import com.web.store.order.feign.CartClient;
import com.web.store.order.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartClient cartClient;

    public String placeOrder(String token,
                             String email,
                             PlaceOrderRequest request) {

        CartResponse cart = cartClient.getCart(token);

        if (cart == null ||
                cart.getItems() == null ||
                cart.getItems().isEmpty()) {

            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();

        order.setUserEmail(email);
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setCity(request.getCity());
        order.setCountry(request.getCountry());
        order.setPhone(request.getPhone());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");
        order.setTotalAmount(cart.getTotalAmount());

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItemResponse item : cart.getItems()) {

            OrderItem orderItem = new OrderItem();

            orderItem.setProductId(item.getProductId());
            orderItem.setProductName(item.getProductName());
            orderItem.setImageUrl(item.getImageUrl());
            orderItem.setSize(item.getSize());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());

            orderItem.setOrder(order);

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);

        orderRepository.save(order);

        cartClient.clearCart(token);

        return "Order placed successfully";
    }

    public List<OrderResponse> getMyOrders(String email) {

        List<Order> orders =
                orderRepository.findByUserEmailOrderByOrderDateDesc(email);

        return orders.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OrderResponse getOrder(Long orderId,
                                  String email) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        if (!order.getUserEmail().equals(email)) {
            throw new RuntimeException("Access denied");
        }

        return mapToResponse(order);
    }

    private OrderResponse mapToResponse(Order order) {

        OrderResponse response = new OrderResponse();

        response.setOrderId(order.getId());
        response.setUserEmail(order.getUserEmail());
        response.setStatus(order.getStatus());
        response.setOrderDate(order.getOrderDate());
        response.setTotalAmount(order.getTotalAmount());

        List<OrderItemResponse> itemResponses =
                order.getItems()
                        .stream()
                        .map(item -> {

                            OrderItemResponse dto =
                                    new OrderItemResponse();

                            dto.setProductName(item.getProductName());
                            dto.setImageUrl(item.getImageUrl());
                            dto.setSize(item.getSize());
                            dto.setQuantity(item.getQuantity());
                            dto.setPrice(item.getPrice());

                            dto.setSubtotal(
                                    item.getPrice()
                                            * item.getQuantity());

                            return dto;
                        })
                        .toList();

        response.setItems(itemResponses);

        return response;
    }
}