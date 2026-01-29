package com.coder.rental.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.rental.entity.AutoBrand;
import com.coder.rental.mapper.AutoBrandMapper;
import com.coder.rental.service.IAutoBrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.rental.util.Result;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gaiyi
 * @since 2026-01-03
 */
@Service
public class AutoBrandServiceImpl extends ServiceImpl<AutoBrandMapper, AutoBrand> implements IAutoBrandService {
    @Resource
    private AutoBrandMapper autoBrandMapper;

    @Override
    public Page<AutoBrand> searchByPage(Page<AutoBrand> page, AutoBrand autoBrand) {
        return autoBrandMapper.searchByPage(page, autoBrand);
    }
}
