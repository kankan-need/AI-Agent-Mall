package com.learn.mall.order.mapper;

import com.learn.mall.order.model.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {

    List<OrderItem> listByOrderId(@Param("orderId") Long orderId);

    int save(OrderItem orderItem);
}
