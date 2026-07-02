package com.learn.mall.product.service.impl;

import com.learn.mall.common.response.ResponseEnum;
import com.learn.mall.product.constant.ProductConstant;
import com.learn.mall.product.dto.ChangeShopCartItemDTO;
import com.learn.mall.product.dto.CheckShopCartItemDTO;
import com.learn.mall.product.mapper.ShopCartItemMapper;
import com.learn.mall.product.mapper.SkuMapper;
import com.learn.mall.product.mapper.SpuMapper;
import com.learn.mall.product.model.ShopCartItem;
import com.learn.mall.product.model.Sku;
import com.learn.mall.product.model.Spu;
import com.learn.mall.product.service.ShopCartService;
import com.learn.mall.product.vo.ShopCartItemVO;
import com.learn.mall.product.vo.ShopCartWithAmountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class ShopCartServiceImpl implements ShopCartService {

    @Autowired
    private ShopCartItemMapper shopCartItemMapper;
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SkuMapper skuMapper;

    @Override
    public ShopCartWithAmountVO info(Long userId) {
        List<ShopCartItemVO> items = getShopCartItems(userId);
        long total = 0L;
        for (ShopCartItemVO item : items) {
            item.setTotalAmount(item.getPriceFee() * item.getCount());
            if (Objects.equals(item.getIsChecked(), 1)) {
                total += item.getTotalAmount();
            }
        }
        ShopCartWithAmountVO vo = new ShopCartWithAmountVO();
        vo.setItems(items);
        vo.setTotalMoney(total);
        return vo;
    }

    @Override
    public List<ShopCartItemVO> getShopCartItems(Long userId) {
        return shopCartItemMapper.listByUserId(userId);
    }

    @Override
    public List<ShopCartItemVO> listCheckedItems(Long userId) {
        return shopCartItemMapper.listCheckedByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeItem(Long userId, ChangeShopCartItemDTO dto) {
        Spu spu = spuMapper.getById(dto.getSpuId());
        Sku sku = skuMapper.getById(dto.getSkuId());
        if (spu == null || sku == null
                || !Objects.equals(spu.getStatus(), ProductConstant.STATUS_ENABLE)
                || !Objects.equals(sku.getStatus(), ProductConstant.STATUS_ENABLE)
                || !Objects.equals(sku.getSpuId(), spu.getSpuId())) {
            throw new com.learn.mall.common.exception.LearnMallException(ResponseEnum.SPU_NOT_EXIST);
        }

        ShopCartItem exist = shopCartItemMapper.getByUserAndSku(userId, dto.getSkuId());
        if (exist != null) {
            int newCount = exist.getCount() + dto.getCount();
            if (newCount <= 0) {
                shopCartItemMapper.deleteByIds(userId, Collections.singletonList(exist.getCartItemId()));
                return;
            }
            exist.setCount(newCount);
            exist.setPriceFee(sku.getPriceFee());
            shopCartItemMapper.update(exist);
            return;
        }

        if (dto.getCount() <= 0) {
            return;
        }

        ShopCartItem item = new ShopCartItem();
        item.setUserId(userId);
        item.setShopId(ProductConstant.DEFAULT_SHOP_ID);
        item.setSpuId(dto.getSpuId());
        item.setSkuId(dto.getSkuId());
        item.setCount(dto.getCount());
        item.setPriceFee(sku.getPriceFee());
        item.setIsChecked(1);
        shopCartItemMapper.save(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteItems(Long userId, List<Long> cartItemIds) {
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            return;
        }
        shopCartItemMapper.deleteByIds(userId, cartItemIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkItems(Long userId, List<CheckShopCartItemDTO> params) {
        if (params == null) {
            return;
        }
        for (CheckShopCartItemDTO param : params) {
            shopCartItemMapper.updateChecked(userId, param.getCartItemId(), param.getIsChecked());
        }
    }

    @Override
    public int prodCount(Long userId) {
        return shopCartItemMapper.countByUserId(userId);
    }
}
