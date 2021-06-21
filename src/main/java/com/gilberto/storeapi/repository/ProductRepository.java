package com.gilberto.storeapi.repository;

import com.gilberto.storeapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}