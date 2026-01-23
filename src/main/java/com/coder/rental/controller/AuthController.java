package com.coder.rental.controller;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTPayload;
import com.coder.rental.entity.Permission;
import com.coder.rental.entity.User;
import com.coder.rental.util.RouteTreeUtil;
import com.coder.rental.vo.RouteVo;
import com.coder.rental.vo.UserInfoVo;
import com.coder.rental.security.CustomerAuthenticationException;
import com.coder.rental.service.IUserService;
import com.coder.rental.util.JWTUtil;
import com.coder.rental.util.RedisUtil;
import com.coder.rental.util.Result;
import com.coder.rental.vo.TokenVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("rental/auth")
public class AuthController {
    @Resource
    private RedisUtil redisUtil;

    @Resource
    private IUserService userService;

    @PostMapping("refreshToken")
    public Result refreshToken(HttpServletRequest request) {
        // 从请求中获取token
        String token = request.getHeader("token");
        if (StrUtil.isEmpty(token)) {
            token = request.getParameter("token");
        }

        // 从Security框架中获取UserDetails对象，用来和请求里的token进行比对
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 从token中解析username和userId属性，用来后面重新生成token
        JWTPayload payload = JWTUtil.parseToken(token);
        String username = payload.getClaim("username").toString();
        NumberWithFormat numberWithFormat = (NumberWithFormat) payload.getClaim("userId");
        Integer userId = numberWithFormat.intValue();

        // 比对请求的用户和UserDetails的用户是否为同一个用户，创建新的token
        String newToken;
        if (StrUtil.equals(userDetails.getUsername(), username)) {
            Map<String, Object> map = new HashMap<>() {{
                put("username", username);
                put("userId", userId);
            }};
            newToken = JWTUtil.createToken(map);
        } else {
            throw new CustomerAuthenticationException("token异常");
        }

        // 从新的token中获取到过期时间并设置到redis中去，因为我们是使用UserId来区分用户的，所以这里直接set覆盖即可
        NumberWithFormat claim = (NumberWithFormat) JWTUtil.parseToken(newToken).getClaim(JWTPayload.EXPIRES_AT);
        DateTime datetime = (DateTime) claim.convert(DateTime.class, claim);
        Long expireTime = datetime.getTime();
        String tokenKey = "token:" + userId;
        redisUtil.set(tokenKey, newToken, expireTime);

        // 返回自定义token返回对象
        TokenVo tokenVo = new TokenVo(newToken, expireTime);
        return Result.success(tokenVo).setMessage("刷新token成功");
    }

    @GetMapping("info")
    public Result<UserInfoVo> getUserInfo() {
        // 从令牌中获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new CustomerAuthenticationException("认证信息为空");
        }
        User user = (User) authentication.getPrincipal();

        // 封装用户视图信息
        Object[] roles = userService.selectRoleName(user.getId()).toArray();
        UserInfoVo userInfo = new UserInfoVo(user.getId(), user.getUsername(),
                user.getNickname(), user.getAvatar(), roles);

        // 返回封装结果
        return Result.success(userInfo).setMessage("获取用户信息成功");
    }

    @GetMapping("menuList")
    public Result<List<RouteVo>> getMenuList() {
        // 从令牌中获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new CustomerAuthenticationException("认证信息为空");
        }
        // 获取到用户信息
        User user = (User) authentication.getPrincipal();

        // 获取权限列表
        List<Permission> permissionList = user.getPermissionList();
        // 将用户拥有的权限列表进行筛选，因为是侧边栏菜单选项，不需要按钮，所以将type为2的permission过滤掉
        permissionList = permissionList.stream()
                .filter(permission -> permission.getPermissionType() != 2)
                .toList();
        // 将权限列表转为视图实体的结构
        List<RouteVo> routeVoList = RouteTreeUtil.buildRouteTree(permissionList, 0);
        return Result.success(routeVoList).setMessage("获取菜单数据成功");
    }
}
