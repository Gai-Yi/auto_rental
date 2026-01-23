package com.coder.rental.mapper;

import com.coder.rental.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gaiyi
 * @since 2026-01-03
 */
public interface UserMapper extends BaseMapper<User> {

    List<String> selectRoleName(Integer id);
}
