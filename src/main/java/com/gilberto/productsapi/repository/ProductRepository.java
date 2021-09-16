package com.gilberto.productsapi.repository;

import com.gilberto.productsapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}