package com.example.ingemark.service;

import com.example.ingemark.api.ProductReq;
import com.example.ingemark.api.ProductResp;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    ResponseEntity<Object> getProductByCode(String code);

    ResponseEntity<Object> getProductById(Long id);

    ResponseEntity<List<ProductResp>> getProductList(int page, int size);

    ResponseEntity<Object> createProduct(ProductReq req);

}
