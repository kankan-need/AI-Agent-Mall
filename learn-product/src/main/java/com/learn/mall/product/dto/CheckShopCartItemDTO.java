package com.learn.mall.product.dto;

import jakarta.validation.constraints.NotNull;

public class CheckShopCartItemDTO {

    @NotNull(message = "cartItemId不能为空")
    private Long cartItemId;
    @NotNull(message = "isChecked不能为空")
    private Integer isChecked;

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Integer getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Integer isChecked) {
        this.isChecked = isChecked;
    }
}
