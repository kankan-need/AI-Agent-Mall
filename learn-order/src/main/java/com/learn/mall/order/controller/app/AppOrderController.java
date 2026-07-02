package com.learn.mall.order.controller.app;

import com.learn.mall.common.database.dto.PageDTO;
import com.learn.mall.common.database.vo.PageVO;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.common.security.AuthUserContext;
import com.learn.mall.order.constant.OrderConstant;
import com.learn.mall.order.dto.OrderSearchDTO;
import com.learn.mall.order.dto.OrderSubmitDTO;
import com.learn.mall.order.service.OrderService;
import com.learn.mall.order.vo.OrderPayInfoVO;
import com.learn.mall.order.vo.OrderVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/a/order")
public class AppOrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public ServerResponseEntity<Long> submit(@Valid @RequestBody OrderSubmitDTO dto) {
        Long userId = AuthUserContext.get().getUserId();
        return ServerResponseEntity.success(orderService.submit(userId, dto));
    }

    @GetMapping("/list")
    public ServerResponseEntity<PageVO<OrderVO>> list(PageDTO pageDTO, Integer status) {
        Long userId = AuthUserContext.get().getUserId();
        OrderSearchDTO search = new OrderSearchDTO();
        search.setUserId(userId);
        search.setStatus(status);
        return ServerResponseEntity.success(orderService.page(pageDTO, search));
    }

    @GetMapping("/detail")
    public ServerResponseEntity<OrderVO> detail(@RequestParam Long orderId) {
        Long userId = AuthUserContext.get().getUserId();
        return ServerResponseEntity.success(orderService.detail(orderId, userId));
    }

    @GetMapping("/pay_info")
    public ServerResponseEntity<OrderPayInfoVO> payInfo(@RequestParam Long orderId) {
        Long userId = AuthUserContext.get().getUserId();
        return ServerResponseEntity.success(orderService.payInfo(userId, orderId));
    }

    @PostMapping("/pay")
    public ServerResponseEntity<Void> pay(@RequestParam Long orderId) {
        Long userId = AuthUserContext.get().getUserId();
        orderService.pay(userId, orderId);
        return ServerResponseEntity.success();
    }

    @PutMapping("/cancel/{orderId}")
    public ServerResponseEntity<Void> cancel(@PathVariable Long orderId) {
        Long userId = AuthUserContext.get().getUserId();
        orderService.cancel(userId, orderId, OrderConstant.CLOSE_USER);
        return ServerResponseEntity.success();
    }
}
