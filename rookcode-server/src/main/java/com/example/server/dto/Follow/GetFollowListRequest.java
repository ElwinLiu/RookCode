package com.example.server.dto.Follow;

/**
 * @author YFMan
 * @Description 请求获取关注或被关注列表
 * @Date 2023/5/6 15:56
 */
public class GetFollowListRequest {
    String account;
    Integer pageNum;
    Integer pageSize;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
