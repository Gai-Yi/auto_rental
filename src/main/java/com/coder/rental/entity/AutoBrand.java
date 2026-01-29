package com.coder.rental.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author gaiyi
 * @since 2026-01-03
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("auto_brand")
@ApiModel(value = "AutoBrand对象", description = "")
public class AutoBrand implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("品牌id")
    private Integer id;

    @ApiModelProperty("厂商id")
    private Integer makerId;

    @ApiModelProperty("品牌名称")
    private String brandName;

    @ApiModelProperty("是否删除 0未删除 1已删除")
    private Boolean deleted;

    @TableField(exist = false)
    private String makerName;
}
