package com.learn.mall.product.mapper;

import com.learn.mall.product.model.SpuDetail;
import org.apache.ibatis.annotations.Param;

public interface SpuDetailMapper {

    SpuDetail getBySpuId(@Param("spuId") Long spuId);

    int save(SpuDetail spuDetail);

    int update(SpuDetail spuDetail);
}
