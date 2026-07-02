package com.learn.mall.order.service;

import com.learn.mall.api.product.vo.ShopCartItemFeignVO;
import com.learn.mall.api.user.vo.UserAddrFeignVO;

import java.util.List;

public interface OrderTransactionService {

    Long persistOrder(Long userId, List<ShopCartItemFeignVO> cartItems, UserAddrFeignVO userAddr);
}
