package com.learn.mall.order.service.impl;

import com.learn.mall.api.product.feign.ProductFeignClient;
import com.learn.mall.api.product.vo.ShopCartItemFeignVO;
import com.learn.mall.api.user.vo.UserAddrFeignVO;
import com.learn.mall.common.util.BeanUtil;
import com.learn.mall.order.constant.OrderConstant;
import com.learn.mall.order.mapper.OrderAddrMapper;
import com.learn.mall.order.mapper.OrderItemMapper;
import com.learn.mall.order.mapper.OrderMapper;
import com.learn.mall.order.model.Order;
import com.learn.mall.order.model.OrderAddr;
import com.learn.mall.order.model.OrderItem;
import com.learn.mall.order.service.OrderTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderTransactionServiceImpl implements OrderTransactionService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderAddrMapper orderAddrMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long persistOrder(Long userId, List<ShopCartItemFeignVO> cartItems, UserAddrFeignVO userAddr) {
        OrderAddr orderAddr = BeanUtil.map(userAddr, OrderAddr.class);
        orderAddr.setUserId(userId);
        orderAddrMapper.save(orderAddr);

        long total = 0L;
        int allCount = 0;
        for (ShopCartItemFeignVO item : cartItems) {
            total += item.getPriceFee() * item.getCount();
            allCount += item.getCount();
        }

        Order order = new Order();
        order.setShopId(OrderConstant.DEFAULT_SHOP_ID);
        order.setUserId(userId);
        order.setShopName(OrderConstant.DEFAULT_SHOP_NAME);
        order.setTotal(total);
        order.setStatus(OrderConstant.STATUS_UNPAY);
        order.setAllCount(allCount);
        order.setIsPayed(0);
        order.setOrderAddrId(orderAddr.getOrderAddrId());
        orderMapper.save(order);

        List<Long> cartItemIds = new ArrayList<>();
        for (ShopCartItemFeignVO item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setShopId(OrderConstant.DEFAULT_SHOP_ID);
            orderItem.setOrderId(order.getOrderId());
            orderItem.setCategoryId(item.getCategoryId());
            orderItem.setSpuId(item.getSpuId());
            orderItem.setSkuId(item.getSkuId());
            orderItem.setUserId(userId);
            orderItem.setCount(item.getCount());
            orderItem.setSpuName(item.getSpuName());
            orderItem.setPic(item.getMainImgUrl());
            orderItem.setPrice(item.getPriceFee());
            orderItem.setSpuTotalAmount(item.getPriceFee() * item.getCount());
            orderItemMapper.save(orderItem);
            cartItemIds.add(item.getCartItemId());
        }

        productFeignClient.deleteCartItems(userId, cartItemIds);
        return order.getOrderId();
    }
}
