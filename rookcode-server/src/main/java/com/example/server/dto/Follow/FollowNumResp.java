package com.example.server.dto.Follow;

/**
 * @author YFMan
 * @Description 返回前端 关注者 数量和 被关注者 数量
 * @Date 2023/5/5 23:58
 */
public class FollowNumResp {
    Integer followeeNum;
    Integer followerNum;

    public Integer getFolloweeNum() {
        return followeeNum;
    }

    public void setFolloweeNum(Integer followeeNum) {
        this.followeeNum = followeeNum;
    }

    public Integer getFollowerNum() {
        return followerNum;
    }

    public void setFollowerNum(Integer followerNum) {
        this.followerNum = followerNum;
    }
}
