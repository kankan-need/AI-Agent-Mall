package com.learn.mall.product.service;

import com.learn.mall.product.dto.ChangeShopCartItemDTO;
import com.learn.mall.product.dto.CheckShopCartItemDTO;
import com.learn.mall.product.vo.ShopCartItemVO;
import com.learn.mall.product.vo.ShopCartWithAmountVO;

import java.util.List;

public interface ShopCartService {

    ShopCartWithAmountVO info(Long userId);

    void changeItem(Long userId, ChangeShopCartItemDTO dto);

    void deleteItems(Long userId, List<Long> cartItemIds);

    void checkItems(Long userId, List<CheckShopCartItemDTO> params);

    int prodCount(Long userId);

    List<ShopCartItemVO> getShopCartItems(Long userId);
}
