package com.example.ingemark.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<ProductEntity, Long> {
    ProductEntity findByCode(String code);
    boolean existsByCode(String code);
}
