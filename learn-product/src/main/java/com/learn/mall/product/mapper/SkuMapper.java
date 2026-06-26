package com.learn.mall.product.mapper;

import com.learn.mall.product.model.Sku;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SkuMapper {

    Sku getById(@Param("skuId") Long skuId);

    Sku getBySpuId(@Param("spuId") Long spuId);

    List<Sku> listBySpuId(@Param("spuId") Long spuId);

    int save(Sku sku);

    int update(Sku sku);
}
