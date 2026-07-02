package com.learn.mall.order.model;

import java.time.LocalDateTime;

public class Order {

    private Long orderId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long shopId;
    private Long userId;
    private String shopName;
    private Long total;
    private Integer status;
    private Integer allCount;
    private LocalDateTime payTime;
    private LocalDateTime cancelTime;
    private Integer isPayed;
    private Integer closeType;
    private Long orderAddrId;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Long getShopId() { return shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }
    public Long getTotal() { return total; }
    public void setTotal(Long total) { this.total = total; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getAllCount() { return allCount; }
    public void setAllCount(Integer allCount) { this.allCount = allCount; }
    public LocalDateTime getPayTime() { return payTime; }
    public void setPayTime(LocalDateTime payTime) { this.payTime = payTime; }
    public LocalDateTime getCancelTime() { return cancelTime; }
    public void setCancelTime(LocalDateTime cancelTime) { this.cancelTime = cancelTime; }
    public Integer getIsPayed() { return isPayed; }
    public void setIsPayed(Integer isPayed) { this.isPayed = isPayed; }
    public Integer getCloseType() { return closeType; }
    public void setCloseType(Integer closeType) { this.closeType = closeType; }
    public Long getOrderAddrId() { return orderAddrId; }
    public void setOrderAddrId(Long orderAddrId) { this.orderAddrId = orderAddrId; }
}
