package com.coder.rental.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.rental.entity.Permission;
import com.coder.rental.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.rental.vo.RolePermissionVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaiyi
 * @since 2026-01-03
 */
public interface IRoleService extends IService<Role> {
    Page<Role> selectPage(Page<Role> page, Role role);

    Boolean hasUser(Integer id);

    Boolean delete(String ids);

    Boolean assignPermission(Integer id, List<Integer> permissionIds);
}
