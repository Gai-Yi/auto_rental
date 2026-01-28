package com.coder.rental.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.rental.entity.AutoMaker;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaiyi
 * @since 2026-01-03
 */
public interface IAutoMakerService extends IService<AutoMaker> {

    Page<AutoMaker> selectPages(int start, int size, AutoMaker autoMaker);
}
