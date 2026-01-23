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
@TableName("sys_dept")
@ApiModel(value = "Dept对象", description = "")
public class Dept implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("部门id")
    private Integer id;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("部门联系电话")
    private String phone;

    @ApiModelProperty("部门地址")
    private String address;

    @ApiModelProperty("父级部门id")
    private Integer pid;

    @ApiModelProperty("父级部门名称")
    private String parentName;

    @ApiModelProperty("排序号")
    private Integer orderNum;

    @ApiModelProperty("是否删除")
    private Boolean deleted;
}
