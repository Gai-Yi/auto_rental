package com.coder.rental.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
@TableName("busi_customer")
@ApiModel(value = "Customer对象", description = "")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("客户id")
    private Integer id;

    @ApiModelProperty("客户姓名")
    private String name;

    @ApiModelProperty("客户年龄")
    private Integer age;

    @ApiModelProperty("客户手机号码")
    private String phone;

    @ApiModelProperty("客户出生日期")
    private LocalDate birthday;

    @ApiModelProperty("客户身份证号码")
    private String idCard;

    @ApiModelProperty("客户状态 0黑名单 1白名单")
    private Boolean status;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("是否删除")
    private Boolean deleted;
}
