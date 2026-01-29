package com.coder.rental.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coder.rental.entity.Dept;
import com.coder.rental.mapper.DeptMapper;
import com.coder.rental.service.IDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.rental.util.DeptTreeUtil;
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
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    @Override
    public List<Dept> selectList(Dept dept) {
        QueryWrapper<Dept> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotEmpty(dept.getDeptName()), "dept_name", dept.getDeptName())
                .orderByAsc("order_num");
        List<Dept> list = baseMapper.selectList(wrapper);
        return DeptTreeUtil.buildDeptTree(list, 0);
    }

    @Override
    public List<Dept> selectTree() {
        QueryWrapper<Dept> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("order_num");
        List<Dept> list = baseMapper.selectList(wrapper);
        // 在集合中添加一个根节点，展开树形目录
        list.add(new Dept().setDeptName("所有部门").setId(0).setPid(-1));
        return DeptTreeUtil.buildDeptTree(list, -1);
    }

    @Override
    public boolean hasChildren(int id) {
        QueryWrapper<Dept> wrapper = new QueryWrapper<>();
        wrapper.eq("pid", id);
        return baseMapper.selectCount(wrapper) > 0;
    }
}
