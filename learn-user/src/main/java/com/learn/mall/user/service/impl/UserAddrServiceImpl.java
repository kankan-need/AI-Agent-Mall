package com.learn.mall.user.service.impl;

import com.learn.mall.user.mapper.UserAddrMapper;
import com.learn.mall.user.model.UserAddr;
import com.learn.mall.user.service.UserAddrService;
import com.learn.mall.user.vo.UserAddrVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserAddrServiceImpl implements UserAddrService {

    @Autowired
    private UserAddrMapper userAddrMapper;

    @Override
    public List<UserAddrVO> list(Long userId) {
        return userAddrMapper.list(userId);
    }

    @Override
    public UserAddrVO getUserAddrByUserId(Long addrId, Long userId) {
        if (addrId == null || addrId == 0L) {
            return userAddrMapper.getUserDefaultAddrByUserId(userId);
        }
        return userAddrMapper.getByAddrId(addrId, userId);
    }

    @Override
    public int countByUserId(Long userId) {
        return userAddrMapper.countByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(UserAddr userAddr) {
        if (UserAddr.DEFAULT_ADDR.equals(userAddr.getIsDefault())) {
            userAddrMapper.removeDefaultUserAddr(userAddr.getUserId());
        }
        userAddrMapper.save(userAddr);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserAddr userAddr) {
        if (UserAddr.DEFAULT_ADDR.equals(userAddr.getIsDefault())) {
            userAddrMapper.removeDefaultUserAddr(userAddr.getUserId());
        }
        userAddrMapper.update(userAddr);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserAddrByUserId(Long addrId, Long userId) {
        userAddrMapper.deleteById(addrId, userId);
    }
}
