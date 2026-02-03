package com.coder.rental.vo;

import com.coder.rental.entity.Permission;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class RolePermissionVo {
    // 角色已有的权限id数组，用于做数据回显
    private Object[] checkedList;

    // 当前用户的权限集合
    private List<Permission> permissionList;
}
