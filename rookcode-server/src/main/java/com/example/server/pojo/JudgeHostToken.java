package com.example.server.pojo;

/**
 * @author YFMan
 * @Description 请求 JudgeHost 服务器的 Token
 * @Date 2023/4/24 21:05
 */
public class JudgeHostToken {
    private String userId;
    private String userSecret;

    // 必须要添加无参构造函数

    public JudgeHostToken() {
    }

    public JudgeHostToken(String userid, String userSecret) {
        this.userId = userid;
        this.userSecret = userSecret;
    }

    public void setUserId(String userid) {
        this.userId = userid;
    }

    public void setUserSecret(String userSecret) {
        this.userSecret = userSecret;
    }

    public String getUserid() {
        return userId;
    }

    public String getUserSecret() {
        return userSecret;
    }
}
