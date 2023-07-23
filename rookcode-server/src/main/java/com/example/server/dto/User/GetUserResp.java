package com.example.server.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserResp {
    // 用户的id
    private Integer id;
    // 用户的邮箱账号
    private String account;
    // 用户的昵称
    private String nickname;
    // 用户头像的url
    private String avatar;
    // 用户是否位管理员
    private byte[] isAdmin;
}
