package com.learn.mall.product.feign;

import com.learn.mall.api.product.dto.StockLockItemDTO;
import com.learn.mall.api.product.feign.ProductFeignClient;
import com.learn.mall.api.product.vo.ShopCartItemFeignVO;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.common.util.BeanUtil;
import com.learn.mall.product.service.ShopCartService;
import com.learn.mall.product.service.StockService;
import com.learn.mall.product.vo.ShopCartItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductFeignController implements ProductFeignClient {

    @Autowired
    private ShopCartService shopCartService;
    @Autowired
    private StockService stockService;

    @Override
    public ServerResponseEntity<List<ShopCartItemFeignVO>> listCheckedCartItems(Long userId) {
        List<ShopCartItemVO> items = shopCartService.listCheckedItems(userId);
        List<ShopCartItemFeignVO> result = new ArrayList<>(items.size());
        for (ShopCartItemVO item : items) {
            result.add(BeanUtil.map(item, ShopCartItemFeignVO.class));
        }
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> deleteCartItems(Long userId, List<Long> cartItemIds) {
        shopCartService.deleteItems(userId, cartItemIds);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> lockStock(List<StockLockItemDTO> items) {
        stockService.lockStock(items);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> unlockStock(List<StockLockItemDTO> items) {
        stockService.unlockStock(items);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> confirmStock(List<StockLockItemDTO> items) {
        stockService.confirmStock(items);
        return ServerResponseEntity.success();
    }
}
