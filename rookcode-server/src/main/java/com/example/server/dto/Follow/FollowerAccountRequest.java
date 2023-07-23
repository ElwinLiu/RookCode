package com.example.server.dto.Follow;

/**
 * @author YFMan
 * @Description 关注别人或取消关注别人的请求
 * @Date 2023/5/6 15:47
 */
public class FollowerAccountRequest {
    String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
