package com.coder.rental.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.rental.entity.AutoBrand;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.rental.util.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaiyi
 * @since 2026-01-03
 */
public interface IAutoBrandService extends IService<AutoBrand> {

    Page<AutoBrand> searchByPage(Page<AutoBrand> page, AutoBrand autoBrand);
}
