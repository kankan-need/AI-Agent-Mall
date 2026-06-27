package com.learn.mall.user.service;

import com.learn.mall.user.model.UserAddr;
import com.learn.mall.user.vo.UserAddrVO;

import java.util.List;

public interface UserAddrService {

    List<UserAddrVO> list(Long userId);

    UserAddrVO getUserAddrByUserId(Long addrId, Long userId);

    int countByUserId(Long userId);

    void save(UserAddr userAddr);

    void update(UserAddr userAddr);

    void deleteUserAddrByUserId(Long addrId, Long userId);
}
