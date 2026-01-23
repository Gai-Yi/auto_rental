package com.coder.rental.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("busi_violation")
@ApiModel(value = "Violation对象", description = "")
public class Violation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("违章id")
    private Integer id;

    @ApiModelProperty("车辆id")
    private Integer autoId;

    @ApiModelProperty("违章时间")
    private LocalDateTime violationTime;

    @ApiModelProperty("违章原因")
    private String reason;

    @ApiModelProperty("违章地点")
    private String location;

    @ApiModelProperty("扣分")
    private Integer deductPoints;

    @ApiModelProperty("罚款")
    private Integer fine;

    @ApiModelProperty("是否处理 0未处理 1已处理")
    private Boolean status;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("是否删除 0未删除 1已删除")
    private Boolean deleted;
}
