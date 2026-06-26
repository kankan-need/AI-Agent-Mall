package com.learn.mall.product.dto;

import jakarta.validation.constraints.NotNull;

public class ChangeShopCartItemDTO {

    @NotNull(message = "spuId不能为空")
    private Long spuId;
    @NotNull(message = "skuId不能为空")
    private Long skuId;
    private Long shopId;
    @NotNull(message = "count不能为空")
    private Integer count;

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
