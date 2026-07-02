package com.learn.mall.order.vo;

import java.time.LocalDateTime;
import java.util.List;

public class OrderVO {

    private Long orderId;
    private Long userId;
    private String shopName;
    private Long total;
    private Integer status;
    private Integer allCount;
    private Integer isPayed;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
    private LocalDateTime cancelTime;
    private Integer closeType;
    private OrderAddrVO orderAddr;
    private List<OrderItemVO> orderItems;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
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
    public Integer getIsPayed() { return isPayed; }
    public void setIsPayed(Integer isPayed) { this.isPayed = isPayed; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getPayTime() { return payTime; }
    public void setPayTime(LocalDateTime payTime) { this.payTime = payTime; }
    public LocalDateTime getCancelTime() { return cancelTime; }
    public void setCancelTime(LocalDateTime cancelTime) { this.cancelTime = cancelTime; }
    public Integer getCloseType() { return closeType; }
    public void setCloseType(Integer closeType) { this.closeType = closeType; }
    public OrderAddrVO getOrderAddr() { return orderAddr; }
    public void setOrderAddr(OrderAddrVO orderAddr) { this.orderAddr = orderAddr; }
    public List<OrderItemVO> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItemVO> orderItems) { this.orderItems = orderItems; }
}
