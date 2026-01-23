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
@TableName("busi_order")
@ApiModel(value = "Order对象", description = "")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单id")
    private Integer id;

    @ApiModelProperty("订单编号")
    private String orderNum;

    @ApiModelProperty("车辆id")
    private Integer autoId;

    @ApiModelProperty("客户id")
    private Integer customerId;

    @ApiModelProperty("出租时间")
    private LocalDateTime rentalTime;

    @ApiModelProperty("出租类型id")
    private Integer typeId;

    @ApiModelProperty("租金")
    private Integer rent;

    @ApiModelProperty("押金")
    private Integer deposit;

    @ApiModelProperty("起租里程")
    private Integer mileage;

    @ApiModelProperty("归还时间")
    private LocalDateTime returnTime;

    @ApiModelProperty("归还里程")
    private Integer returnMileage;

    @ApiModelProperty("应付金额")
    private Integer rentPayable;

    @ApiModelProperty("实付金额")
    private Integer rentActual;

    @ApiModelProperty("押金退还 0未退还 1已退还")
    private Boolean depositReturn;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("是否删除 0未删除 1已删除")
    private Boolean deleted;
}
