package com.example.productservice.controllers;

import com.example.productservice.model.dto.ProductRequest;
import com.example.productservice.model.dto.ProductResponse;
import com.example.productservice.services.ProductService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @GetMapping
    public Slice<ProductResponse> getAllProducts(@RequestParam(name = "name",required = false) String name,Pageable pageable){
        return productService.getAllProducts(pageable,name);
    }
    @PutMapping()
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody ProductRequest productRequest){
        return new ResponseEntity<>(productService.updateProduct(productRequest),HttpStatus.OK);
    }
    @PutMapping("/makeOrder")
    public ResponseEntity<List<ProductResponse>> reduceQuantity(Map<Long,Integer> order){
        return new ResponseEntity<>(productService.reduceQuantity(order),HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<ProductResponse> addNewProduct(@RequestBody @Validated ProductRequest productRequest){
        return new ResponseEntity<>(productService.addNewProduct(productRequest),HttpStatus.CREATED);
    }

}
