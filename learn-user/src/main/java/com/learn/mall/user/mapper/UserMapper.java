package com.learn.mall.user.mapper;

import com.learn.mall.user.model.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    User getByUserId(@Param("userId") Long userId);

    int save(User user);

    int update(User user);
}
