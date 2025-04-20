package com.project.ecommerceApi.Repository;

import com.project.ecommerceApi.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
