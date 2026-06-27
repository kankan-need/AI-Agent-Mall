package com.learn.mall.user.controller.app;

import com.learn.mall.common.exception.LearnMallException;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.common.security.AuthUserContext;
import com.learn.mall.common.util.BeanUtil;
import com.learn.mall.user.dto.UserAddrDTO;
import com.learn.mall.user.model.UserAddr;
import com.learn.mall.user.service.UserAddrService;
import com.learn.mall.user.vo.UserAddrVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/a/user_addr")
public class UserAddrController {

    private static final int MAX_USER_ADDR = 10;

    @Autowired
    private UserAddrService userAddrService;

    @GetMapping("/list")
    public ServerResponseEntity<List<UserAddrVO>> list() {
        Long userId = AuthUserContext.get().getUserId();
        return ServerResponseEntity.success(userAddrService.list(userId));
    }

    @GetMapping
    public ServerResponseEntity<UserAddrVO> getByAddrId(@RequestParam Long addrId) {
        Long userId = AuthUserContext.get().getUserId();
        return ServerResponseEntity.success(userAddrService.getUserAddrByUserId(addrId, userId));
    }

    @PostMapping
    public ServerResponseEntity<Void> save(@Valid @RequestBody UserAddrDTO userAddrDTO) {
        Long userId = AuthUserContext.get().getUserId();
        int count = userAddrService.countByUserId(userId);
        if (count >= MAX_USER_ADDR) {
            return ServerResponseEntity.showFailMsg("收货地址已达到上限，无法再新增地址");
        }
        UserAddr userAddr = BeanUtil.map(userAddrDTO, UserAddr.class);
        if (count == 0) {
            userAddr.setIsDefault(UserAddr.DEFAULT_ADDR);
        } else if (!UserAddr.DEFAULT_ADDR.equals(userAddr.getIsDefault())) {
            userAddr.setIsDefault(UserAddr.NOT_DEFAULT_ADDR);
        }
        userAddr.setAddrId(null);
        userAddr.setUserId(userId);
        userAddrService.save(userAddr);
        return ServerResponseEntity.success();
    }

    @PutMapping
    public ServerResponseEntity<Void> update(@Valid @RequestBody UserAddrDTO userAddrDTO) {
        Long userId = AuthUserContext.get().getUserId();
        UserAddrVO dbUserAddr = userAddrService.getUserAddrByUserId(userAddrDTO.getAddrId(), userId);
        if (dbUserAddr == null) {
            throw new LearnMallException("该地址已被删除");
        }
        if (UserAddr.DEFAULT_ADDR.equals(dbUserAddr.getIsDefault())
                && UserAddr.NOT_DEFAULT_ADDR.equals(userAddrDTO.getIsDefault())) {
            throw new LearnMallException("默认地址不能改为普通地址");
        }
        UserAddr userAddr = BeanUtil.map(userAddrDTO, UserAddr.class);
        userAddr.setUserId(userId);
        userAddrService.update(userAddr);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    public ServerResponseEntity<Void> delete(@RequestParam Long addrId) {
        Long userId = AuthUserContext.get().getUserId();
        UserAddrVO dbUserAddr = userAddrService.getUserAddrByUserId(addrId, userId);
        if (dbUserAddr == null) {
            throw new LearnMallException("该地址已被删除");
        }
        if (UserAddr.DEFAULT_ADDR.equals(dbUserAddr.getIsDefault())) {
            throw new LearnMallException("默认地址不能删除");
        }
        userAddrService.deleteUserAddrByUserId(addrId, userId);
        return ServerResponseEntity.success();
    }
}
