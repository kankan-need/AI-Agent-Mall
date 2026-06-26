package com.learn.mall.product.controller.app;

import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.common.security.AuthUserContext;
import com.learn.mall.product.dto.ChangeShopCartItemDTO;
import com.learn.mall.product.dto.CheckShopCartItemDTO;
import com.learn.mall.product.service.ShopCartService;
import com.learn.mall.product.vo.ShopCartWithAmountVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/a/shop_cart")
public class ShopCartController {

    @Autowired
    private ShopCartService shopCartService;

    @GetMapping("/info")
    public ServerResponseEntity<ShopCartWithAmountVO> info() {
        Long userId = AuthUserContext.get().getUserId();
        return ServerResponseEntity.success(shopCartService.info(userId));
    }

    @PostMapping("/change_item")
    public ServerResponseEntity<Void> changeItem(@Valid @RequestBody ChangeShopCartItemDTO param) {
        Long userId = AuthUserContext.get().getUserId();
        shopCartService.changeItem(userId, param);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/delete_item")
    public ServerResponseEntity<Void> deleteItem(@RequestBody List<Long> shopCartItemIds) {
        Long userId = AuthUserContext.get().getUserId();
        shopCartService.deleteItems(userId, shopCartItemIds);
        return ServerResponseEntity.success();
    }

    @PostMapping("/check_items")
    public ServerResponseEntity<Void> checkItems(@Valid @RequestBody List<CheckShopCartItemDTO> params) {
        Long userId = AuthUserContext.get().getUserId();
        shopCartService.checkItems(userId, params);
        return ServerResponseEntity.success();
    }

    @GetMapping("/prod_count")
    public ServerResponseEntity<Integer> prodCount() {
        Long userId = AuthUserContext.get().getUserId();
        return ServerResponseEntity.success(shopCartService.prodCount(userId));
    }
}
