package com.learn.mall.product.vo;

import java.util.List;

public class ShopCartWithAmountVO {

    private List<ShopCartItemVO> items;
    private Long totalMoney;

    public List<ShopCartItemVO> getItems() {
        return items;
    }

    public void setItems(List<ShopCartItemVO> items) {
        this.items = items;
    }

    public Long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Long totalMoney) {
        this.totalMoney = totalMoney;
    }
}
