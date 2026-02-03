package com.coder.rental.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.rental.entity.Permission;
import com.coder.rental.entity.Role;
import com.coder.rental.entity.User;
import com.coder.rental.entity.UserRole;
import com.coder.rental.mapper.RoleMapper;
import com.coder.rental.mapper.UserMapper;
import com.coder.rental.mapper.UserRoleMapper;
import com.coder.rental.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.rental.vo.RolePermissionVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public Page<Role> selectPage(Page<Role> page, Role role) {
        // 根据名字查询，然后按照创建时间排序
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotEmpty(role.getRoleName()), "role_name", role.getRoleName())
                .orderByAsc("create_time");

        // 根据当前操作用户是否为管理员，来判断是否可以查询所有角色
        Integer createId = role.getCreateId();
        User user = userMapper.selectById(createId);
        if (user != null && user.getIsAdmin() != 1) {
            wrapper.eq("create_id", role.getCreateId());
        }
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public Boolean hasUser(Integer id) {
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", id);
        return userRoleMapper.selectCount(wrapper) > 0;
    }

    @Override
    public Boolean delete(String ids) {
        Arrays.stream(ids.split(",")).map(Integer::parseInt).forEach(
                id -> {
                    if (!hasUser(id)) {
                        userRoleMapper.delete(new QueryWrapper<UserRole>().eq("role_id", id));
                        baseMapper.deleteById(id);
                    }
                }
        );
        return true;
    }

    @Override
    public Boolean assignPermission(Integer id, List<Integer> permissionIds) {
        // 删除当前角色的所有权限
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", id);
        userRoleMapper.delete(wrapper);

        // 将permissionList中的权限加入到当前角色中
        permissionIds.forEach(permissionId -> {
            UserRole userRole = new UserRole(id, permissionId);
            userRoleMapper.insert(userRole);
        });
        return true;
    }
}
