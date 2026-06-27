package com.learn.mall.user.controller.app;

import cn.hutool.core.util.StrUtil;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.common.security.AuthUserContext;
import com.learn.mall.user.model.User;
import com.learn.mall.user.service.UserService;
import com.learn.mall.user.vo.UserSimpleInfoVO;
import com.learn.mall.user.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/a/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/simple_info")
    public ServerResponseEntity<UserSimpleInfoVO> simpleInfo() {
        Long userId = AuthUserContext.get().getUserId();
        UserVO userVO = userService.getByUserId(userId);
        UserSimpleInfoVO vo = new UserSimpleInfoVO();
        if (userVO != null) {
            vo.setNickName(userVO.getNickName());
            vo.setPic(userVO.getPic());
        }
        return ServerResponseEntity.success(vo);
    }

    @GetMapping("/ma/user_detail_info")
    public ServerResponseEntity<UserVO> userDetailInfo() {
        Long userId = AuthUserContext.get().getUserId();
        return ServerResponseEntity.success(userService.getByUserId(userId));
    }

    @PostMapping("/ma/update_user")
    public ServerResponseEntity<Void> updateUser(@RequestBody UserVO userVO) {
        Long userId = AuthUserContext.get().getUserId();
        UserVO dbUser = userService.getByUserId(userId);
        User user = new User();
        user.setUserId(userId);
        user.setNickName(StrUtil.isBlank(userVO.getNickName()) ? dbUser.getNickName() : userVO.getNickName());
        user.setPic(StrUtil.isBlank(userVO.getPic()) ? dbUser.getPic() : userVO.getPic());
        userService.update(user);
        return ServerResponseEntity.success();
    }
}
