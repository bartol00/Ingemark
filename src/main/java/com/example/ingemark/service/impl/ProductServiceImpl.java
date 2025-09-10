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
    public ResponseEntity<Object> getProductByCode(String code) {
        ProductEntity productEntity = productDao.findByCode(code);
        if (productEntity != null) {
            return ResponseEntity.ok(mapper.toProductResp(productEntity));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The product with this code could not be found!");
    }

    @Override
    public ResponseEntity<Object> getProductById(Long id) {
        Optional<ProductEntity> productEntity = productDao.findById(id);
        if (productEntity.isPresent()) {
            return ResponseEntity.ok(productEntity.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The product with this ID could not be found!");
    }

    @Override
    public ResponseEntity<List<ProductResp>> getProductList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
        List<ProductEntity> productEntityList = productDao.findAll(pageRequest).getContent();
        return ResponseEntity.ok(mapper.toProductRespList(productEntityList));
    }

    @Override
    public ResponseEntity<Object> createProduct(ProductReq req) {
        if (productDao.existsByCode(req.getCode())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Product with this unique code already exists!");
        }
        Float price_usd = getEurToUsd(req.getPrice_eur());
        if (price_usd == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not get current EUR to USD conversion rate!");
        }
        ProductEntity productEntity = mapper.toProductEntity(req);
        productEntity.setPrice_usd(price_usd);
        productEntity = productDao.save(productEntity);
        return ResponseEntity.ok(mapper.toProductResp(productEntity));
    }

    private Float getEurToUsd(float eur) {
        try {
            JsonNode body = webClient.get()
                    .uri("https://api.hnb.hr/tecajn-eur/v3?valuta=USD")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (body != null) {
                String srednjiTecajStr = body.get(0).get("srednji_tecaj").asText();
                return Float.parseFloat(srednjiTecajStr.replace(",", ".")) * eur;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
