package com.learn.mall.order.model;

import java.time.LocalDateTime;

public class OrderItem {

    private Long orderItemId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long shopId;
    private Long orderId;
    private Long categoryId;
    private Long spuId;
    private Long skuId;
    private Long userId;
    private Integer count;
    private String spuName;
    private String pic;
    private Long price;
    private Long spuTotalAmount;

    public Long getOrderItemId() { return orderItemId; }
    public void setOrderItemId(Long orderItemId) { this.orderItemId = orderItemId; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Long getShopId() { return shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Long getSpuId() { return spuId; }
    public void setSpuId(Long spuId) { this.spuId = spuId; }
    public Long getSkuId() { return skuId; }
    public void setSkuId(Long skuId) { this.skuId = skuId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
    public String getSpuName() { return spuName; }
    public void setSpuName(String spuName) { this.spuName = spuName; }
    public String getPic() { return pic; }
    public void setPic(String pic) { this.pic = pic; }
    public Long getPrice() { return price; }
    public void setPrice(Long price) { this.price = price; }
    public Long getSpuTotalAmount() { return spuTotalAmount; }
    public void setSpuTotalAmount(Long spuTotalAmount) { this.spuTotalAmount = spuTotalAmount; }
}
