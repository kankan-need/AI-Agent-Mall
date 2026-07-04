package com.learn.mall.product.service;

import com.learn.mall.product.model.Coupon;

import java.util.List;

public interface CouponService {

    List<Coupon> adminList();

    List<Coupon> availableList();

    Coupon getById(Long couponId);

    void save(Coupon coupon);

    void update(Coupon coupon);

    void delete(Long couponId);
}
