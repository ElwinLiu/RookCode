package com.example.server.dto.User;

import lombok.Data;

@Data
public class RegisterRequest {
    // 新建账号
    private String account;
    // 新建密码
    private String password;
    // 邮箱验证码
    private String code;
}
