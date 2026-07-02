package com.learn.mall.order.task;

import com.learn.mall.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderCancelTask {

    @Autowired
    private OrderService orderService;

    @Scheduled(fixedRate = 60000)
    public void cancelExpiredOrders() {
        orderService.cancelExpiredOrders();
    }
}
