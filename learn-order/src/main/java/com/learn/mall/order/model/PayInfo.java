package com.learn.mall.order.model;

import java.time.LocalDateTime;

public class PayInfo {

    private Long payId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long userId;
    private String orderIds;
    private String bizPayNo;
    private Integer payStatus;
    private Long payAmount;
    private LocalDateTime confirmTime;

    public Long getPayId() { return payId; }
    public void setPayId(Long payId) { this.payId = payId; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getOrderIds() { return orderIds; }
    public void setOrderIds(String orderIds) { this.orderIds = orderIds; }
    public String getBizPayNo() { return bizPayNo; }
    public void setBizPayNo(String bizPayNo) { this.bizPayNo = bizPayNo; }
    public Integer getPayStatus() { return payStatus; }
    public void setPayStatus(Integer payStatus) { this.payStatus = payStatus; }
    public Long getPayAmount() { return payAmount; }
    public void setPayAmount(Long payAmount) { this.payAmount = payAmount; }
    public LocalDateTime getConfirmTime() { return confirmTime; }
    public void setConfirmTime(LocalDateTime confirmTime) { this.confirmTime = confirmTime; }
}
