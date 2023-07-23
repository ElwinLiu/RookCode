package com.example.server.dto.Follow;

import java.util.List;

/**
 * @author YFMan
 * @Description TODO
 * @Date 2023/5/7 11:03
 */
public class FollowRespV3 {
    List<FollowEntityV3> followList;
    Long pageTotalNum;

    public List<FollowEntityV3> getFollowList() {
        return followList;
    }

    public void setFollowList(List<FollowEntityV3> followList) {
        this.followList = followList;
    }

    public Long getPageTotalNum() {
        return pageTotalNum;
    }

    public void setPageTotalNum(Long pageTotalNum) {
        this.pageTotalNum = pageTotalNum;
    }
}
