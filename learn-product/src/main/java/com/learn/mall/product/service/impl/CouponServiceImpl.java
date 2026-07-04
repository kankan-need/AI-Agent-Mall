package com.learn.mall.product.service.impl;

import com.learn.mall.common.exception.LearnMallException;
import com.learn.mall.product.mapper.CouponMapper;
import com.learn.mall.product.model.Coupon;
import com.learn.mall.product.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public List<Coupon> adminList() {
        return couponMapper.list(null);
    }

    @Override
    public List<Coupon> availableList() {
        return couponMapper.list(1);
    }

    @Override
    public Coupon getById(Long couponId) {
        return couponMapper.getById(couponId);
    }

    @Override
    public void save(Coupon coupon) {
        coupon.setStatus(coupon.getStatus() == null ? 1 : coupon.getStatus());
        couponMapper.save(coupon);
    }

    @Override
    public void update(Coupon coupon) {
        Coupon exist = couponMapper.getById(coupon.getCouponId());
        if (exist == null) {
            throw new LearnMallException("优惠券不存在");
        }
        couponMapper.update(coupon);
    }

    @Override
    public void delete(Long couponId) {
        couponMapper.delete(couponId);
    }
}
