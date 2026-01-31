package com.coder.rental.util;

import com.coder.rental.entity.Permission;
import com.coder.rental.vo.RouteVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RouteTreeUtil {
    public static List<RouteVo> buildRouteTree(List<Permission> permissionList, int pid) {
        List<RouteVo> routeVoList = new ArrayList<>();
        // 包装permissList，避免对空集合进行操作，orElse表示如果为空就新建一个集合
        Optional.ofNullable(permissionList)
                .orElse(new ArrayList<>())
                .stream()
                // 过滤，从集合中筛选元素进行操作，比如这里时筛选permission不为空，且父id是给定的父id的数据
                // 注意，退出条件也隐含在这里，当集合里面没有元素符合条件的时候，返回空，后面的foreach将不会执行，也就不会再递归下去
                .filter(permission -> permission != null && permission.getPid() == pid)
                // 对筛选后的集合进行遍历
                .forEach(permission -> {
                    // 设置视图实体的数据
                    RouteVo routeVo = new RouteVo();
                    routeVo.setPath(permission.getRoutePath());
                    routeVo.setName(permission.getRouteName());
                    // 根据是否为根组件来设置component和是否展示的值
                    if (permission.getPid() == 0) {
                        routeVo.setComponent("Layout");
                        routeVo.setAlwaysShow(true);
                    } else {
                        routeVo.setComponent(permission.getRouteUrl());
                        routeVo.setAlwaysShow(false);
                    }
                    // 设置实体元数据
                    routeVo.setMeta(routeVo.new Meta(
                            permission.getPermissionLabel(),
                            permission.getIcon(),
                            permission.getPermissionCode().split(",")));

                    // 对当前permission进行递归，使用当前permission的id作为下一轮的父id，如果没有子数据了，那么会退出循环
                    List<RouteVo> children = buildRouteTree(permissionList, permission.getId());
                    // 递归的结果就是当前permission的子数据
                    routeVo.setChildren(children);
                    // 将这条数据加入到结果集合中，这个集合对应着一组下拉选项
                    routeVoList.add(routeVo);
                });
        return routeVoList;
    }

    public static List<Permission> buildPermissionTree(List<Permission> permissionList, int pid) {
        List<Permission> permissionTree = new ArrayList<>();
        Optional.ofNullable(permissionList)
                .orElse(new ArrayList<>())
                .stream()
                .filter(permission -> permission != null && permission.getPid() == pid)
                .forEach(permission -> {
                    // 拷贝一份
                    Permission menu = new Permission();
                    BeanUtils.copyProperties(permission, menu);
                    menu.setChildren(buildPermissionTree(permissionList, menu.getId()));
                    permissionTree.add(menu);
                });
        return permissionTree;
    }
}
