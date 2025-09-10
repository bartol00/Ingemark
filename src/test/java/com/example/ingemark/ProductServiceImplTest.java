package com.example.ingemark;

import com.example.ingemark.api.ProductReq;
import com.example.ingemark.api.ProductResp;
import com.example.ingemark.mapper.ProductMapper;
import com.example.ingemark.model.ProductDao;
import com.example.ingemark.model.ProductEntity;
import com.example.ingemark.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.Optional;
import java.util.Random;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void getProductByCode_whenProductExists_shouldReturnOk() {
        String code = generateRandomCode();

        ProductEntity entity = new ProductEntity();
        entity.setCode(code);

        ProductResp resp = new ProductResp();
        resp.setCode(code);

        when(productDao.findByCode(code)).thenReturn(entity);
        when(productMapper.toProductResp(entity)).thenReturn(resp);

        ResponseEntity<Object> response = productService.getProductByCode(code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resp, response.getBody());
    }

    @Test
    void getProductByCode_whenProductDoesNotExist_shouldReturnNotFound() {
        String code = generateRandomCode();

        when(productDao.findByCode(code)).thenReturn(null);

        ResponseEntity<Object> response = productService.getProductByCode(code);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("The product with this code could not be found!", response.getBody());
    }

    @Test
    void getProductById_whenProductExists_shouldReturnOk() {
        Long id = 1L;

        ProductEntity entity = new ProductEntity();
        entity.setId(id);

        when(productDao.findById(id)).thenReturn(Optional.of(entity));

        ResponseEntity<Object> response = productService.getProductById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(entity, response.getBody());
    }

    @Test
    void getProductById_whenProductDoesNotExist_shouldReturnNotFound() {
        Long id = 999L;

        when(productDao.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = productService.getProductById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("The product with this ID could not be found!", response.getBody());
    }

    @Test
    void createProduct_whenProductDoesNotExist_shouldReturnOk() {
        String code = generateRandomCode();

        ProductReq req = createProductReq(code);

        ProductEntity entity = new ProductEntity();
        entity.setCode(code);

        ProductResp resp = new ProductResp();
        resp.setCode(code);

        when(productDao.existsByCode(code)).thenReturn(false);
        when(productMapper.toProductEntity(req)).thenReturn(entity);
        when(productDao.save(any())).thenReturn(entity);
        when(productMapper.toProductResp(entity)).thenReturn(resp);

        ResponseEntity<Object> response = productService.createProduct(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(ProductResp.class, response.getBody());
        verify(productDao).save(any());
    }

    @Test
    void createProduct_whenProductCodeExists_shouldReturnConflict() {
        String code = generateRandomCode();

        ProductReq req = createProductReq(code);

        when(productDao.existsByCode(code)).thenReturn(true);

        ResponseEntity<Object> response = productService.createProduct(req);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Product with this unique code already exists!", response.getBody());
        verify(productDao, never()).save(any());
    }

    private String generateRandomCode() {
        int length = 10;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private ProductReq createProductReq(String code) {
        ProductReq req = new ProductReq();
        req.setCode(code);
        // other components of the request are not that important as they do not have to be unique
        req.setPrice_eur(1.00f);
        req.setName("TEST PRODUCT");
        req.setIs_available(true);
        return req;
    }

}
