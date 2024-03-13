package com.example.productservice.model.repository;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
    <T> Slice<T> findAllBy(Class<T> type, Pageable pageable);
    <T> Slice<T> findAllByProductNameContainingIgnoreCase(Class<T> type, Pageable pageable,String name);
    Optional<ProductEntity> findProductEntityByProductName(String productName);
}
