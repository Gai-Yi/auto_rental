package com.coder.rental.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResult {
    private Integer id;
    private Integer code;
    private String token;
    private Long expireTime;
}
