package com.learn.mall.auth.mapper;

import com.learn.mall.api.auth.vo.AuthAccountVO;
import com.learn.mall.auth.model.AuthAccount;
import com.learn.mall.common.security.bo.AuthAccountInVerifyBO;
import org.apache.ibatis.annotations.Param;

public interface AuthAccountMapper {

    AuthAccountInVerifyBO getAuthAccountInVerifyByInputUserName(@Param("inputUserName") String inputUserName,
                                                                @Param("sysType") Integer sysType);

    AuthAccountVO getByUsernameAndSysType(@Param("username") String username, @Param("sysType") Integer sysType);

    Long getMaxUid();

    int save(AuthAccount authAccount);
}
