package com.learn.mall.user.service;

import com.learn.mall.user.dto.UserRegisterDTO;
import com.learn.mall.user.model.User;
import com.learn.mall.user.vo.UserVO;

public interface UserService {

    Long save(UserRegisterDTO param);

    UserVO getByUserId(Long userId);

    void update(User user);
}
