package com.project.ecommerceApi.Repository;

import com.project.ecommerceApi.Entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
