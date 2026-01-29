package com.coder.rental.service;

import com.coder.rental.entity.Dept;
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
public interface IDeptService extends IService<Dept> {

    List<Dept> selectList(Dept dept);

    List<Dept> selectTree();

    boolean hasChildren(int id);
}
