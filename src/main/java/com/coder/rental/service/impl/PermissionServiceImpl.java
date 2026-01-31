package com.coder.rental.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coder.rental.entity.Permission;
import com.coder.rental.mapper.PermissionMapper;
import com.coder.rental.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.rental.util.RouteTreeUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gaiyi
 * @since 2026-01-03
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

    @Override
    public List<Permission> selectPermissionListByUserId(Integer userId) {
        return baseMapper.selectPermissionListByUserId(userId);
    }

    @Override
    public boolean hasChildren(int id) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.eq("pid", id);
        return baseMapper.selectCount(wrapper) > 0;
    }

    @Override
    public List<Permission> selectTree() {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        // 排除按钮菜单
        wrapper.ne("permission_type", 2).orderByAsc("order_num");
        List<Permission> permissions = baseMapper.selectList(wrapper);
        Permission  permission = new Permission();
        permission.setId(0).setPid(-1).setPermissionLabel("根目录");
        permissions.add(permission);
        return RouteTreeUtil.buildPermissionTree(permissions, 0);
    }

    @Override
    public List<Permission> selectList() {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("order_num");
        List<Permission> permissions = baseMapper.selectList(wrapper);
        return RouteTreeUtil.buildPermissionTree(permissions, 0);
    }
}
