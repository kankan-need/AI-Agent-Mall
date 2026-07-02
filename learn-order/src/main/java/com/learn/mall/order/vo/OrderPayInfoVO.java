package com.learn.mall.order.vo;

import java.time.LocalDateTime;
import java.util.List;

public class OrderPayInfoVO {

    private Long orderId;
    private Long total;
    private LocalDateTime createTime;
    private LocalDateTime expireTime;
    private OrderAddrVO orderAddr;
    private List<OrderItemVO> orderItems;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getTotal() { return total; }
    public void setTotal(Long total) { this.total = total; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getExpireTime() { return expireTime; }
    public void setExpireTime(LocalDateTime expireTime) { this.expireTime = expireTime; }
    public OrderAddrVO getOrderAddr() { return orderAddr; }
    public void setOrderAddr(OrderAddrVO orderAddr) { this.orderAddr = orderAddr; }
    public List<OrderItemVO> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItemVO> orderItems) { this.orderItems = orderItems; }
}
