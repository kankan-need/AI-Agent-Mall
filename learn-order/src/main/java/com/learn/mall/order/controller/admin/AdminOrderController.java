package com.learn.mall.order.controller.admin;

import com.learn.mall.common.database.dto.PageDTO;
import com.learn.mall.common.database.vo.PageVO;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.order.dto.OrderSearchDTO;
import com.learn.mall.order.service.OrderService;
import com.learn.mall.order.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/order")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/page")
    public ServerResponseEntity<PageVO<OrderVO>> page(PageDTO pageDTO, Integer status) {
        OrderSearchDTO search = new OrderSearchDTO();
        search.setStatus(status);
        return ServerResponseEntity.success(orderService.page(pageDTO, search));
    }

    @GetMapping("/{orderId}")
    public ServerResponseEntity<OrderVO> detail(@PathVariable Long orderId) {
        return ServerResponseEntity.success(orderService.detail(orderId, null));
    }
}
