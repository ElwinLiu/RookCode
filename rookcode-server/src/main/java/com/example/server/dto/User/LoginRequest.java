package com.example.server.dto.User;

import lombok.Data;

@Data
public class LoginRequest {
    // 用户账号
    private String account;
    // 用户密码
    private String password;
    // 登录验证码
    private String code;
}
