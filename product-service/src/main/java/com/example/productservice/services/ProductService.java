package com.example.productservice.services;

import com.example.productservice.model.dto.ProductRequest;
import com.example.productservice.model.dto.ProductResponse;
import com.example.productservice.model.repository.ProductEntity;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductService {
    Slice<ProductResponse> getAllProducts(Pageable pageable,String name);

    ProductResponse addNewProduct(ProductRequest productRequest);
    ProductEntity getProductById(Long id);

    ProductResponse updateProduct(ProductRequest productRequest);

    List<ProductResponse> reduceQuantity(Map<Long,Integer> order);
}
