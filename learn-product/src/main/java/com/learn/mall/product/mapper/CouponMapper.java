package com.learn.mall.product.mapper;

import com.learn.mall.product.model.Coupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CouponMapper {

    List<Coupon> list(@Param("status") Integer status);

    Coupon getById(@Param("couponId") Long couponId);

    int save(Coupon coupon);

    int update(Coupon coupon);

    int delete(@Param("couponId") Long couponId);
}
