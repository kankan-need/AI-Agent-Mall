package com.learn.mall.product.mapper;

import com.learn.mall.product.model.ShopCartItem;
import com.learn.mall.product.vo.ShopCartItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopCartItemMapper {

    List<ShopCartItemVO> listByUserId(@Param("userId") Long userId);

    List<ShopCartItemVO> listCheckedByUserId(@Param("userId") Long userId);

    ShopCartItem getByUserAndSku(@Param("userId") Long userId, @Param("skuId") Long skuId);

    int save(ShopCartItem item);

    int update(ShopCartItem item);

    int deleteByIds(@Param("userId") Long userId, @Param("ids") List<Long> ids);

    int deleteAll(@Param("userId") Long userId);

    int updateChecked(@Param("userId") Long userId, @Param("cartItemId") Long cartItemId,
                      @Param("isChecked") Integer isChecked);

    int countByUserId(@Param("userId") Long userId);
}
