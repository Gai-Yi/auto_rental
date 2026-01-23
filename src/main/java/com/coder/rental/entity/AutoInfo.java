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
@TableName("auto_info")
@ApiModel(value = "AutoInfo对象", description = "")
public class AutoInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("车辆信息id")
    private Integer id;

    @ApiModelProperty("车牌号码")
    private Integer autoNum;

    @ApiModelProperty("厂商id")
    private Integer makerId;

    @ApiModelProperty("品牌id")
    private Integer brandId;

    @ApiModelProperty("车辆类型")
    private String infoType;

    @ApiModelProperty("车辆颜色")
    private String color;

    @ApiModelProperty("排量")
    private Double displacement;

    @ApiModelProperty("排量单位")
    private String unit;

    @ApiModelProperty("行驶里程")
    private Integer mileage;

    @ApiModelProperty("租金")
    private Integer rent;

    @ApiModelProperty("上牌时间")
    private LocalDateTime registrationDate;

    @ApiModelProperty("车辆图片")
    private String pic;

    @ApiModelProperty("押金")
    private Integer deposit;

    @ApiModelProperty("0未租 1已租 2维保 3自用")
    private Boolean status;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("应保次数")
    private Integer expectedNum;

    @ApiModelProperty("实保次数")
    private Integer actualNum;

    @ApiModelProperty("是否删除")
    private Boolean deleted;
}
