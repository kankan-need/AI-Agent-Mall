package com.learn.mall.order.dto;

import jakarta.validation.constraints.NotNull;

public class OrderSubmitDTO {

    @NotNull(message = "收货地址不能为空")
    private Long addrId;

    public Long getAddrId() {
        return addrId;
    }

    public void setAddrId(Long addrId) {
        this.addrId = addrId;
    }
}
