package com.coder.rental.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTPayload;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.rental.entity.Role;
import com.coder.rental.entity.User;
import com.coder.rental.service.IPermissionService;
import com.coder.rental.service.IRoleService;
import com.coder.rental.util.JWTUtil;
import com.coder.rental.util.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gaiyi
 * @since 2026-01-03
 */
@RestController
@RequestMapping("/rental/role")
public class RoleController {
    @Resource
    private IRoleService roleService;

    @Resource
    private IPermissionService permissionService;

    @PostMapping("{start}/{size}")
    public Result selectPage(@PathVariable int start,
                             @PathVariable int size,
                             @RequestBody Role role,
                             HttpServletRequest request) {
        // 要根据用户id来查询角色，如果用户是超级管理员，则查询所有角色，否则查询当前用户创建的角色
        // 所以要获取userId
        // 方式一
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getPrincipal();
//        role.setCreateId(user.getId());
        // 方式二
//        String token = request.getHeader("token");
//        if (StrUtil.isEmpty(token)) {
//            token = request.getParameter("token");
//        }
//        if (StrUtil.isEmpty(token)) {
//            return Result.fail().setMessage("token为空");
//        }
//        JWTPayload payload = JWTUtil.parseToken(token);
//        Integer userId = (Integer) payload.getClaim("userId");
//        role.setCreateId(userId);

        Page<Role> page = new Page<>(start, size);
        return Result.success(roleService.selectPage(page, role));
    }

    @PostMapping
    public Result save(@RequestBody Role role) {
        return roleService.save(role) ? Result.success() : Result.fail();
    }

    @PutMapping
    public Result update(@RequestBody Role role) {
        return roleService.updateById(role) ? Result.success() : Result.fail();
    }

    @GetMapping("hasUser/{id}")
    public Result hasUser(@PathVariable Integer id) {
        return roleService.hasUser(id) ? Result.success("有") : Result.success("无");
    }

    @DeleteMapping("{ids}")
    public Result delete(@PathVariable String ids) {
        return roleService.delete(ids) ? Result.success() : Result.fail();
    }

    /*
    分配权限：首先要查询出来当前角色所可以分配的权限，然后查询出来所分配权限的角色所拥有的权限用于做回显
     */
    @GetMapping("{userId}/{roleId}")
    public Result selectPermissionTree(@PathVariable Integer userId,
                                       @PathVariable Integer roleId) {
        return Result.success(permissionService.selectPermissionTree(userId, roleId));
    }

    @PostMapping("assignPermission/{roleId}/{permissionIds}")
    public Result assignPermission(@PathVariable Integer roleId,
                       @PathVariable String permissionIds) {
        List<Integer> list = Arrays.stream(permissionIds.split(",")).map(Integer::parseInt).toList();
        return roleService.assignPermission(roleId, list) ? Result.success() : Result.fail();
    }
}
