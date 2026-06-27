package com.learn.mall.user.mapper;

import com.learn.mall.user.model.UserAddr;
import com.learn.mall.user.vo.UserAddrVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAddrMapper {

    List<UserAddrVO> list(@Param("userId") Long userId);

    UserAddrVO getByAddrId(@Param("addrId") Long addrId, @Param("userId") Long userId);

    UserAddrVO getUserDefaultAddrByUserId(@Param("userId") Long userId);

    int countByUserId(@Param("userId") Long userId);

    int save(UserAddr userAddr);

    int update(UserAddr userAddr);

    int deleteById(@Param("addrId") Long addrId, @Param("userId") Long userId);

    int removeDefaultUserAddr(@Param("userId") Long userId);
}
