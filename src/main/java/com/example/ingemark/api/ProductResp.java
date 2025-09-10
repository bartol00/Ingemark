package com.example.ingemark.api;

import lombok.Data;

@Data
public class ProductResp {

    private Long id;
    private String code, name;
    private Float price_eur, price_usd;
    private Boolean is_available;

}
