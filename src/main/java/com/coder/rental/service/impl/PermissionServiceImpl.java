package com.coder.rental.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coder.rental.entity.Permission;
import com.coder.rental.entity.User;
import com.coder.rental.mapper.PermissionMapper;
import com.coder.rental.mapper.UserMapper;
import com.coder.rental.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.rental.util.RouteTreeUtil;
import com.coder.rental.vo.RolePermissionVo;
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
    @Resource
    private UserMapper userMapper;

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

    @Override
    public RolePermissionVo selectPermissionTree(Integer userId, Integer roleId) {
        // 查询当前用户所拥有的权限列表，用来做分配权限的选项
        User user = userMapper.selectById(userId);
        List<Permission> list = null;

        // 如果是管理员查询所有数据，不是管理员则查询当前用户所拥有的权限
        if (user != null && user.getIsAdmin() == 1) {
            list = baseMapper.selectList(null);
        } else {
            list = baseMapper.selectPermissionListByUserId(userId);
        }

        // 构建树状权限列表
        List<Permission> permissionList = RouteTreeUtil.buildPermissionTree(list, 0);

        // 查询当前角色所拥有的权限，用来做数据的回显
        List<Permission> rolePermissionList = baseMapper.selectPermissionListByRoleId(roleId);

        // 获取角色权限id
        Object[] checkedList = rolePermissionList.stream().map(Permission::getId).toArray();

        return new RolePermissionVo().setCheckedList(checkedList).setPermissionList(permissionList);
    }
}
