package com.learn.mall.order.service;

import com.learn.mall.common.database.dto.PageDTO;
import com.learn.mall.common.database.vo.PageVO;
import com.learn.mall.order.dto.OrderSearchDTO;
import com.learn.mall.order.dto.OrderSubmitDTO;
import com.learn.mall.order.vo.OrderPayInfoVO;
import com.learn.mall.order.vo.OrderVO;

public interface OrderService {

    Long submit(Long userId, OrderSubmitDTO dto);

    void cancel(Long userId, Long orderId, Integer closeType);

    void pay(Long userId, Long orderId);

    OrderPayInfoVO payInfo(Long userId, Long orderId);

    PageVO<OrderVO> page(PageDTO pageDTO, OrderSearchDTO search);

    OrderVO detail(Long orderId, Long userId);

    void cancelExpiredOrders();
}
