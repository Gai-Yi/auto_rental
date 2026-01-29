package com.coder.rental.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.rental.entity.AutoBrand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.rental.util.Result;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gaiyi
 * @since 2026-01-03
 */
public interface AutoBrandMapper extends BaseMapper<AutoBrand> {

    Page<AutoBrand> searchByPage(@Param("page") Page<AutoBrand> page, @Param("autoBrand") AutoBrand autoBrand);
}
