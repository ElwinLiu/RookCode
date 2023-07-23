package com.example.server.dto.Follow;

import java.util.List;

/**
 * @author YFMan
 * @Description 获取关注或被关注列表的响应
 * @Date 2023/5/4 20:24
 */
public class FollowResp {
    List<FollowEntity> followList;
    Long pageTotalNum;

    public List<FollowEntity> getFollowList() {
        return followList;
    }

    public void setFollowList(List<FollowEntity> followList) {
        this.followList = followList;
    }

    public Long getPageTotalNum() {
        return pageTotalNum;
    }

    public void setPageTotalNum(Long pageTotalNum) {
        this.pageTotalNum = pageTotalNum;
    }
}
