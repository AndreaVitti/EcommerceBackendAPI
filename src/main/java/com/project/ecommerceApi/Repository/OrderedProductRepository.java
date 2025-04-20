package com.project.ecommerceApi.Repository;

import com.project.ecommerceApi.Entity.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {
}
