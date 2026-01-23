package com.coder.rental.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <p>
 * 
 * </p>
 *
 * @author gaiyi
 * @since 2026-01-03
 */
@Data
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value = "User对象", description = "")
public class User implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private Integer id;

    @ApiModelProperty("用户账号")
    private String username;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("账户是否没有过期")
    private boolean isAccountNonExpired = true;

    @ApiModelProperty("账户是否锁定")
    private boolean isAccountNonLocked = true;

    @ApiModelProperty("密码是否过期")
    private boolean isCredentialsNonExpired = true;

    @ApiModelProperty("账户是否可用")
    private boolean isEnabled = true;

    @ApiModelProperty("真实姓名")
    private String realname;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("部门id")
    private Integer deptId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("性别 0女 1男")
    private Boolean gender;

    @ApiModelProperty("用户手机号码")
    private String phone;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("是否是管理员")
    private Boolean isAdmin;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("是否删除 0未删除 1已删除")
    private Boolean deleted;

    // 添加一个字段，因为继承UserDetail需要实现对应的get方法
    @TableField(exist = false)
    private Collection<? extends GrantedAuthority> authorities;

    // 获取权限菜单
    @TableField(exist = false)
    private List<Permission> permissionList;
}
