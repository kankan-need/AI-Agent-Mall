package com.learn.mall.product.controller.admin;

import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.product.model.Coupon;
import com.learn.mall.product.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/coupon")
public class AdminCouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping("/list")
    public ServerResponseEntity<List<Coupon>> list() {
        return ServerResponseEntity.success(couponService.adminList());
    }

    @GetMapping
    public ServerResponseEntity<Coupon> getById(@RequestParam Long couponId) {
        return ServerResponseEntity.success(couponService.getById(couponId));
    }

    @PostMapping
    public ServerResponseEntity<Void> save(@RequestBody Coupon coupon) {
        couponService.save(coupon);
        return ServerResponseEntity.success();
    }

    @PutMapping
    public ServerResponseEntity<Void> update(@RequestBody Coupon coupon) {
        couponService.update(coupon);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    public ServerResponseEntity<Void> delete(@RequestParam Long couponId) {
        couponService.delete(couponId);
        return ServerResponseEntity.success();
    }
}
