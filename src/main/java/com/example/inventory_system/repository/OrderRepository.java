package com.example.inventory_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inventory_system.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}