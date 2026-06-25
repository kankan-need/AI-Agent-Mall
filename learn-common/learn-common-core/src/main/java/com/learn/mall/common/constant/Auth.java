package com.learn.mall.common.constant;

import com.learn.mall.common.feign.FeignInsideAuthConfig;

public interface Auth {

    String CHECK_TOKEN_URI = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/token/checkToken";
}
