package com.coder.rental.service;

import com.coder.rental.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaiyi
 * @since 2026-01-03
 */
public interface IUserService extends IService<User> {
    User selectByUsername(String username);

    List<String> selectRoleName(Integer id);
}
