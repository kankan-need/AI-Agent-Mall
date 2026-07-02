package com.learn.mall.order.constant;

public final class OrderConstant {

    public static final int STATUS_UNPAY = 1;
    public static final int STATUS_PAYED = 2;
    public static final int STATUS_SUCCESS = 5;
    public static final int STATUS_CLOSE = 6;

    public static final int CLOSE_TIMEOUT = 1;
    public static final int CLOSE_USER = 4;

    public static final int PAY_UNPAID = 0;
    public static final int PAY_PAID = 1;

    public static final long DEFAULT_SHOP_ID = 1L;
    public static final String DEFAULT_SHOP_NAME = "学习商城";

    private OrderConstant() {
    }
}
