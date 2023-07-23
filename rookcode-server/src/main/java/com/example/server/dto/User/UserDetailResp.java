package com.example.server.dto.User;

import com.example.server.dto.Question.LanguageResp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResp {
    // 是否为请求自己的
    boolean is_self;
    String nickname;
    String account;
    String avatar;
    String description;
    int view;
    int like;
    private List<LanguageResp> langs;
    // 判断用户是否关注了该用户
    boolean follow;
}
