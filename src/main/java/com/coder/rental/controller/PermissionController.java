package com.coder.rental.controller;

import com.coder.rental.entity.Permission;
import com.coder.rental.entity.RolePermission;
import com.coder.rental.entity.User;
import com.coder.rental.service.IPermissionService;
import com.coder.rental.service.IRolePermissionService;
import com.coder.rental.util.Result;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gaiyi
 * @since 2026-01-03
 */
@RestController
@RequestMapping("/rental/permission")
public class PermissionController {

    @Resource
    private IPermissionService permissionService;

    @GetMapping
    public Result list() {
        return Result.success(permissionService.selectList());
    }

    @GetMapping("tree")
    public Result tree() {
        return Result.success(permissionService.selectTree());
    }

    @PostMapping
    public Result save(@RequestBody Permission permission) {
        if (permission.getPermissionType() != 2) {
            if (permission.getRouteUrl().contains("/")) {
                permission.setRouteName(permission.getRouteUrl().substring(permission.getRouteUrl().lastIndexOf("/") + 1));
                permission.setRoutePath(permission.getRouteUrl().substring(permission.getRouteUrl().lastIndexOf("/")));
            }
        }
        return permissionService.save(permission) ? Result.success() : Result.fail();
    }

    @PutMapping
    public Result update(@RequestBody Permission permission) {
        if (permission.getPermissionType() != 2) {
            if (permission.getRouteUrl().contains("/")) {
                permission.setRouteName(permission.getRouteUrl().substring(permission.getRouteUrl().lastIndexOf("/") + 1));
                permission.setRoutePath(permission.getRouteUrl().substring(permission.getRouteUrl().lastIndexOf("/")));
            }
        }
        return permissionService.updateById(permission) ? Result.success() : Result.fail();
    }

    @DeleteMapping("{id}")
    public Result delete(@PathVariable int id) {
        return permissionService.removeById(id) ? Result.success() : Result.fail();
    }

    @GetMapping("{id}")
    public Result hasChildren(@PathVariable int id) {
        return permissionService.hasChildren(id) ? Result.success().setMessage("有") : Result.success().setMessage("无");
    }
}
