package com.project.ecommerceApi.Repository;

import com.project.ecommerceApi.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
