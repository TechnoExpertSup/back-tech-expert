package com.example.productservice.services.impl;

import com.example.productservice.exceptions.ProductNotFoundException;
import com.example.productservice.exceptions.ProductUniqueNameException;
import com.example.productservice.model.dto.ProductRequest;
import com.example.productservice.model.dto.ProductResponse;
import com.example.productservice.model.repository.ProductEntity;
import com.example.productservice.model.repository.ProductRepository;
import com.example.productservice.services.ProductService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ModelMapper modelMapper;
    @Transactional(readOnly = true)
    @Override
    public Slice<ProductResponse> getAllProducts(Pageable pageable,String name){
        if(name != null){
            return repository.findAllByProductNameContainingIgnoreCase(ProductResponse.class, pageable, name);
        }
        return repository.findAllBy(ProductResponse.class,pageable);
    }
    @Transactional(readOnly = true)
    @Override
    public ProductEntity getProductById(Long id){
        return Optional.of(repository.getReferenceById(id)).orElseThrow(()-> new ProductNotFoundException("Can`t find product with such id: ",id));
    }
    @Transactional
    @Override
    public ProductResponse addNewProduct(ProductRequest productRequest) {
        var productEntity = modelMapper.map(productRequest, ProductEntity.class);
        try {
            var savedEntity = repository.save(productEntity);
            return mapEntityToResponse(savedEntity);
        } catch (DataIntegrityViolationException e) {
            throw new ProductUniqueNameException("Such product has been already added, please find it using name and update, what you need. Your request: ",
                    productRequest);
        }
    }
    @Transactional
    @Override
    public ProductResponse updateProduct(ProductRequest productRequest) {
        var updatedEntity  = repository.findProductEntityByProductName(productRequest.productName())
                .orElseThrow(()-> new ProductNotFoundException("Product with name: "+ productRequest.productName() +" not found",productRequest));
        updatedEntity.setProductPrice(productRequest.productPrice());
        updatedEntity.setProductQuantity(updatedEntity.getProductQuantity()+productRequest.productQuantity());
        if(!productRequest.productDescription().isEmpty()) {
            updatedEntity.setProductDescription(productRequest.productDescription());
        }
        return mapEntityToResponse(updatedEntity);
    }

    @Override
    @Transactional
    public List<ProductResponse> reduceQuantity(Map<Long,Integer> order) {
        return null;
    }

    private ProductResponse mapEntityToResponse(ProductEntity productEntity){
        return new ProductResponse(productEntity.getProductId(),
                productEntity.getProductName(),
                productEntity.getProductPrice(),
                productEntity.getProductDescription(),
                productEntity.getProductQuantity());
    }
}
