package com.coder.rental.entity;

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
@TableName("auto_maker")
@ApiModel(value = "AutoMaker对象", description = "")
public class AutoMaker implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("厂商id")
    private Integer id;

    @ApiModelProperty("厂商名称")
    private String name;

    @ApiModelProperty("排序字母，使用首字母进行排序分类")
    private String orderLetter;

    @ApiModelProperty("是否删除 0未删除 1已删除")
    private Boolean deleted;
}
