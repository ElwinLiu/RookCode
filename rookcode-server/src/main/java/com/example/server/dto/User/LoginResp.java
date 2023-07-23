package com.example.server.dto.User;

import com.example.server.pojo.Users;
import lombok.Data;

@Data
public class LoginResp {
    // 用户信息
    private Users users;
    // token
    private String token;

    private boolean isAdmin;

    public LoginResp(Users users, String token, boolean isAdmin) {
        this.users = users;
        this.token = token;
        this.isAdmin = isAdmin;
    }
}
