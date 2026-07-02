package com.learn.mall.api.product.feign;

import com.learn.mall.api.product.dto.StockLockItemDTO;
import com.learn.mall.api.product.vo.ShopCartItemFeignVO;
import com.learn.mall.common.feign.FeignInsideAuthConfig;
import com.learn.mall.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "learn-product", contextId = "product")
public interface ProductFeignClient {

    @GetMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/cart/checked_items")
    ServerResponseEntity<List<ShopCartItemFeignVO>> listCheckedCartItems(@RequestParam("userId") Long userId);

    @DeleteMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/cart/delete_items")
    ServerResponseEntity<Void> deleteCartItems(@RequestParam("userId") Long userId,
                                               @RequestBody List<Long> cartItemIds);

    @PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/stock/lock")
    ServerResponseEntity<Void> lockStock(@RequestBody List<StockLockItemDTO> items);

    @PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/stock/unlock")
    ServerResponseEntity<Void> unlockStock(@RequestBody List<StockLockItemDTO> items);

    @PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/stock/confirm")
    ServerResponseEntity<Void> confirmStock(@RequestBody List<StockLockItemDTO> items);
}
