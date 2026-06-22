package com.web.store.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.store.order.entity.Order;

@Repository
public interface OrderRepository
        extends JpaRepository<Order, Long> {

    List<Order> findByUserEmailOrderByOrderDateDesc(
            String userEmail);
}