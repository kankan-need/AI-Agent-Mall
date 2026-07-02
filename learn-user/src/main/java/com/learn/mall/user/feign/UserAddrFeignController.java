package com.learn.mall.user.feign;

import com.learn.mall.api.user.feign.UserAddrFeignClient;
import com.learn.mall.api.user.vo.UserAddrFeignVO;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.common.util.BeanUtil;
import com.learn.mall.user.service.UserAddrService;
import com.learn.mall.user.vo.UserAddrVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAddrFeignController implements UserAddrFeignClient {

    @Autowired
    private UserAddrService userAddrService;

    @Override
    public ServerResponseEntity<UserAddrFeignVO> getAddr(Long addrId, Long userId) {
        UserAddrVO addr = userAddrService.getUserAddrByUserId(addrId, userId);
        return ServerResponseEntity.success(BeanUtil.map(addr, UserAddrFeignVO.class));
    }
}
