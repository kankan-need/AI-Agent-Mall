package com.learn.mall.product.controller.app;

import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.common.security.AuthUserContext;
import com.learn.mall.product.mapper.UserCouponMapper;
import com.learn.mall.product.model.Coupon;
import com.learn.mall.product.model.UserCoupon;
import com.learn.mall.product.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class AppCouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @GetMapping("/ua/coupon/list")
    public ServerResponseEntity<List<Coupon>> list() {
        return ServerResponseEntity.success(couponService.availableList());
    }

    @PostMapping("/a/coupon/claim")
    public ServerResponseEntity<Void> claim(@RequestParam Long couponId) {
        Long userId = AuthUserContext.get().getUserId();

        Coupon coupon = couponService.getById(couponId);
        if (coupon == null || coupon.getStatus() != 1) {
            return ServerResponseEntity.showFailMsg("优惠券不存在或已下架");
        }

        UserCoupon exist = userCouponMapper.getByUserIdAndCouponId(userId, couponId);
        if (exist != null) {
            return ServerResponseEntity.showFailMsg("已领取过该优惠券");
        }

        UserCoupon uc = new UserCoupon();
        uc.setUserId(userId);
        uc.setCouponId(couponId);
        uc.setClaimTime(LocalDateTime.now());
        uc.setExpireTime(LocalDateTime.now().plusDays(coupon.getValidDays()));
        uc.setStatus("unused");
        userCouponMapper.save(uc);

        return ServerResponseEntity.success();
    }

    @GetMapping("/a/coupon/my")
    public ServerResponseEntity<List<UserCoupon>> my() {
        Long userId = AuthUserContext.get().getUserId();
        return ServerResponseEntity.success(userCouponMapper.listByUserId(userId));
    }
}
