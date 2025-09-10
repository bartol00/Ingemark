package com.example.ingemark.mapper;

import com.example.ingemark.api.ProductReq;
import com.example.ingemark.api.ProductResp;
import com.example.ingemark.model.ProductEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    public abstract ProductEntity toProductEntity(ProductReq req);
    public abstract ProductResp toProductResp(ProductEntity entity);
    public abstract List<ProductResp> toProductRespList(List<ProductEntity> list);

}
