package com.example.ingemark.controller;

import com.example.ingemark.api.ProductReq;
import com.example.ingemark.api.ProductResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Product Controller", description = "Endpoints for managing products")
@RequestMapping("/api/product")
public interface ProductController {

    @Operation(summary = "Get product by code", description = "Fetch product details by their unique 10-character codes")
    @GetMapping("/get/code/{code}")
    ResponseEntity<ProductResp> getProductByCode(@PathVariable String code);

    @Operation(summary = "Get product by ID", description = "Fetch product details by their unique numeric IDs")
    @GetMapping("/get/id/{id}")
    ResponseEntity<ProductResp> getProductById(@PathVariable Long id);

    @Operation(summary = "Get product list", description = "Fetch a paginated list of all products, sorted by their numeric IDs (ascending)")
    @GetMapping("/get/list")
    ResponseEntity<List<ProductResp>> getProductList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size);

    @Operation(summary = "Create a new product", description = "Create a new product with a name, price (in EUR), availability status and unique 10-character code")
    @PostMapping("/create")
    ResponseEntity<ProductResp> createProduct(@Valid @RequestBody ProductReq req);

}
