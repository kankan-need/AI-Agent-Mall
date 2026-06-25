package com.learn.mall.auth.mapper;

import com.learn.mall.common.security.bo.AuthAccountInVerifyBO;
import org.apache.ibatis.annotations.Param;

public interface AuthAccountMapper {

    AuthAccountInVerifyBO getAuthAccountInVerifyByInputUserName(@Param("inputUserName") String inputUserName,
                                                                @Param("sysType") Integer sysType);
}
