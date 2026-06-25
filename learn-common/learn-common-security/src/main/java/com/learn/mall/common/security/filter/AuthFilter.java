package com.learn.mall.common.security.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.learn.mall.api.auth.bo.UserInfoInTokenBO;
import com.learn.mall.api.auth.constant.SysTypeEnum;
import com.learn.mall.api.auth.feign.TokenFeignClient;
import com.learn.mall.api.rbac.constant.HttpMethodEnum;
import com.learn.mall.api.rbac.feign.PermissionFeignClient;
import com.learn.mall.common.constant.Auth;
import com.learn.mall.common.feign.FeignInsideAuthConfig;
import com.learn.mall.common.handler.HttpHandler;
import com.learn.mall.common.response.ResponseEnum;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.common.security.AuthUserContext;
import com.learn.mall.common.security.adapter.AuthConfigAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
public class AuthFilter implements Filter {

    @Autowired
    private AuthConfigAdapter authConfigAdapter;
    @Autowired
    private HttpHandler httpHandler;
    @Autowired
    private TokenFeignClient tokenFeignClient;
    @Autowired
    private PermissionFeignClient permissionFeignClient;
    @Autowired
    private FeignInsideAuthConfig feignInsideAuthConfig;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (!feignRequestCheck(req)) {
            httpHandler.printServerResponseToWeb(ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED));
            return;
        }

        if (Auth.CHECK_TOKEN_URI.equals(req.getRequestURI())) {
            chain.doFilter(req, resp);
            return;
        }

        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (String excludePathPattern : authConfigAdapter.excludePathPatterns()) {
            if (pathMatcher.match(excludePathPattern, req.getRequestURI())) {
                chain.doFilter(req, resp);
                return;
            }
        }

        String accessToken = req.getHeader("Authorization");
        if (StrUtil.isBlank(accessToken)) {
            httpHandler.printServerResponseToWeb(ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED));
            return;
        }

        ServerResponseEntity<UserInfoInTokenBO> tokenResponse = tokenFeignClient.checkToken(accessToken);
        if (!tokenResponse.isSuccess()) {
            httpHandler.printServerResponseToWeb(ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED));
            return;
        }

        UserInfoInTokenBO userInfoInToken = tokenResponse.getData();
        if (!checkRbac(userInfoInToken, req.getRequestURI(), req.getMethod())) {
            httpHandler.printServerResponseToWeb(ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED));
            return;
        }

        try {
            AuthUserContext.set(userInfoInToken);
            chain.doFilter(req, resp);
        } finally {
            AuthUserContext.clean();
        }
    }

    private boolean feignRequestCheck(HttpServletRequest req) {
        if (!req.getRequestURI().startsWith(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX)) {
            return true;
        }
        String feignInsideSecret = req.getHeader(feignInsideAuthConfig.getKey());
        return StrUtil.isNotBlank(feignInsideSecret)
                && Objects.equals(feignInsideSecret, feignInsideAuthConfig.getSecret());
    }

    private boolean checkRbac(UserInfoInTokenBO userInfoInToken, String uri, String method) {
        if (!Objects.equals(SysTypeEnum.PLATFORM.value(), userInfoInToken.getSysType())
                && !Objects.equals(SysTypeEnum.MULTISHOP.value(), userInfoInToken.getSysType())) {
            return true;
        }
        ServerResponseEntity<Boolean> response = permissionFeignClient.checkPermission(
                userInfoInToken.getUserId(),
                userInfoInToken.getSysType(),
                uri,
                userInfoInToken.getIsAdmin(),
                HttpMethodEnum.valueOfMethod(method).value());
        return response.isSuccess() && Boolean.TRUE.equals(response.getData());
    }
}
