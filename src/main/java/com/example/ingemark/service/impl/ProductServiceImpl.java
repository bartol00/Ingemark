package com.example.ingemark.service.impl;

import com.example.ingemark.api.ProductReq;
import com.example.ingemark.api.ProductResp;
import com.example.ingemark.mapper.ProductMapper;
import com.example.ingemark.model.ProductDao;
import com.example.ingemark.model.ProductEntity;
import com.example.ingemark.service.ProductService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;
    private final ProductDao productDao;
    private final WebClient webClient = WebClient.create();

    @Override
    public ResponseEntity<ProductResp> getProductByCode(String code) {
        ProductEntity productEntity = productDao.findByCode(code);
        if (productEntity != null) {
            return ResponseEntity.ok(mapper.toProductResp(productEntity));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @Override
    public ResponseEntity<ProductResp> getProductById(Long id) {
        Optional<ProductEntity> productEntity = productDao.findById(id);
        return productEntity.map(entity -> ResponseEntity.ok(mapper.toProductResp(entity))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @Override
    public ResponseEntity<List<ProductResp>> getProductList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
        List<ProductEntity> productEntityList = productDao.findAll(pageRequest).getContent();
        return ResponseEntity.ok(mapper.toProductRespList(productEntityList));
    }

    @Override
    public ResponseEntity<ProductResp> createProduct(ProductReq req) {
        if (productDao.existsByCode(req.getCode())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        ProductEntity productEntity = mapper.toProductEntity(req);
        float price_usd = getEurToUsd(productEntity.getPrice_eur());
        productEntity.setPrice_usd(price_usd);
        productEntity = productDao.save(productEntity);
        return ResponseEntity.ok(mapper.toProductResp(productEntity));
    }

    private float getEurToUsd(float eur) {
        try {
            JsonNode body = webClient.get()
                    .uri("https://api.hnb.hr/tecajn-eur/v3?valuta=USD&datum-primjene=2025-9-10")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (body != null) {
                String srednjiTecajStr = body.get(0).get("srednji_tecaj").asText();
                return Float.parseFloat(srednjiTecajStr.replace(",", ".")) * eur;
            } else {
                return 0.0f;
            }
        } catch (Exception e) {
            return 0.0f;
        }
    }
}
