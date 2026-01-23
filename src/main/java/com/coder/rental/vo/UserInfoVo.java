package com.coder.rental.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVo {
    private Integer id;
    private String name;
    private String introduction;
    private String avatar;
    private Object[] roles;
}
