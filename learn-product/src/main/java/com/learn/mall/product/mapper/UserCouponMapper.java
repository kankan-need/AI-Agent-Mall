package com.learn.mall.product.mapper;

import com.learn.mall.product.model.UserCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserCouponMapper {

    List<UserCoupon> listByUserId(@Param("userId") Long userId);

    UserCoupon getByUserIdAndCouponId(@Param("userId") Long userId, @Param("couponId") Long couponId);

    int save(UserCoupon userCoupon);
}
