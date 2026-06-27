package com.learn.mall.user.service.impl;

import com.learn.mall.api.auth.constant.SysTypeEnum;
import com.learn.mall.api.auth.dto.AuthAccountDTO;
import com.learn.mall.api.auth.feign.AccountFeignClient;
import com.learn.mall.api.auth.vo.AuthAccountVO;
import com.learn.mall.common.exception.LearnMallException;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.common.util.PrincipalUtil;
import com.learn.mall.user.dto.UserRegisterDTO;
import com.learn.mall.user.mapper.UserMapper;
import com.learn.mall.user.model.User;
import com.learn.mall.user.service.UserService;
import com.learn.mall.user.vo.UserVO;
import com.learn.mall.common.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountFeignClient accountFeignClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(UserRegisterDTO param) {
        checkRegisterInfo(param);

        AuthAccountDTO authAccountDTO = new AuthAccountDTO();
        authAccountDTO.setUsername(param.getUserName());
        authAccountDTO.setPassword(param.getPassword());
        authAccountDTO.setCreateIp("127.0.0.1");
        authAccountDTO.setIsAdmin(0);
        authAccountDTO.setSysType(SysTypeEnum.ORDINARY.value());
        authAccountDTO.setStatus(1);
        authAccountDTO.setTenantId(0L);

        ServerResponseEntity<Long> accountResponse = accountFeignClient.save(authAccountDTO);
        if (!accountResponse.isSuccess()) {
            throw new LearnMallException(accountResponse.getMsg());
        }

        Long uid = accountResponse.getData();
        Long userId = uid;

        User user = new User();
        user.setUserId(userId);
        user.setPic(param.getImg());
        user.setNickName(param.getNickName());
        user.setStatus(1);
        userMapper.save(user);

        return uid;
    }

    @Override
    public UserVO getByUserId(Long userId) {
        User user = userMapper.getByUserId(userId);
        return BeanUtil.map(user, UserVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User user) {
        userMapper.update(user);
    }

    private void checkRegisterInfo(UserRegisterDTO param) {
        if (!PrincipalUtil.isUserName(param.getUserName())) {
            throw new LearnMallException("请输入正确的用户名");
        }
        ServerResponseEntity<AuthAccountVO> response = accountFeignClient.getByUsernameAndSysType(
                param.getUserName(), SysTypeEnum.ORDINARY.value());
        if (!response.isSuccess()) {
            throw new LearnMallException(response.getMsg());
        }
        if (Objects.nonNull(response.getData())) {
            throw new LearnMallException("用户名已存在");
        }
    }
}
