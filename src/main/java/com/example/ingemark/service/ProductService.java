package com.example.ingemark.service;

import com.example.ingemark.api.ProductReq;
import com.example.ingemark.api.ProductResp;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    ResponseEntity<ProductResp> getProductByCode(String code);

    ResponseEntity<ProductResp> getProductById(Long id);

    ResponseEntity<List<ProductResp>> getProductList(int page, int size);

    ResponseEntity<ProductResp> createProduct(ProductReq req);

}
