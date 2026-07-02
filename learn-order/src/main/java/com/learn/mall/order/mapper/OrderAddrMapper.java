package com.learn.mall.order.mapper;

import com.learn.mall.order.model.OrderAddr;
import org.apache.ibatis.annotations.Param;

public interface OrderAddrMapper {

    OrderAddr getById(@Param("orderAddrId") Long orderAddrId);

    int save(OrderAddr orderAddr);
}
