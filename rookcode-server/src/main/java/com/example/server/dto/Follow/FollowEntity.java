package com.example.server.dto.Follow;

/**
 * @author YFMan
 * @Description 关注者和被关注者列表 entity
 * @Date 2023/5/4 20:21
 */
public class FollowEntity {
    String nickname;
    String account;
    String avatar;
    String description;

    // 是否互相关注
    boolean mutual;

    // 是否已经关注
    boolean followed;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMutual() {
        return mutual;
    }

    public void setMutual(boolean mutual) {
        this.mutual = mutual;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }
}
