package com.coder.rental.security;

import com.coder.rental.entity.Permission;
import com.coder.rental.entity.User;
import com.coder.rental.service.IPermissionService;
import com.coder.rental.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class CustomerUserDetailsService implements UserDetailsService {
    @Resource
    private IUserService userService;

    @Resource
    private IPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 将数据权限列表查询出来，然后封装到user对象中
        List<Permission> permissions = permissionService.selectPermissionListByUserId(user.getId());
        user.setPermissionList(permissions);

        // 将权限列表中的permissionCode单独查询出来，封装到user对象的authorities属性中
        // 首先批量提取permissionCode到一个列表中
        List<String> list = permissions.stream()
                .filter(new Predicate<Permission>() {
                    @Override
                    public boolean test(Permission permission) {
                        return permission != null;
                    }
                })
                .map(
                        new Function<Permission, String>() {
                            @Override
                            public String apply(Permission permission) {
                                return permission.getPermissionCode();
                            }
                        }
                )
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String permissionCode) {
                        return permissionCode != null;
                    }
                }).toList();
        // 将列表转为字符串数组
        String[] array = list.toArray(new String[list.size()]);
        // 使用工具将权限码封装到authorities中
        List<GrantedAuthority> grantedList = AuthorityUtils.createAuthorityList(array);
        user.setAuthorities(grantedList);

        System.out.println(user.getAuthorities());
        return user;
    }
}
