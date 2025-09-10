package com.example.ingemark.controller.impl;

import com.example.ingemark.api.ProductReq;
import com.example.ingemark.api.ProductResp;
import com.example.ingemark.controller.ProductController;
import com.example.ingemark.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;

    @Override
    public ResponseEntity<Object> getProductByCode(String code) {
        return productService.getProductByCode(code);
    }

    @Override
    public ResponseEntity<Object> getProductById(Long id) {
        return productService.getProductById(id);
    }

    @Override
    public ResponseEntity<List<ProductResp>> getProductList(int page, int size) {
        return productService.getProductList(page, size);
    }

    @Override
    public ResponseEntity<Object> createProduct(ProductReq req) {
        return productService.createProduct(req);
    }

}
