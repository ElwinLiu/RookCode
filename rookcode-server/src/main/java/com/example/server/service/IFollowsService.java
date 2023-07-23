package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.dto.Follow.FollowNumResp;
import com.example.server.dto.Follow.FollowResp;
import com.example.server.dto.Follow.FollowRespV3;
import com.example.server.dto.Resp;
import com.example.server.pojo.Follows;

import java.util.List;

/**
 * @author YFMan
 * @Description 关注表的服务类接口
 * @Date 2023/5/4 18:03
 */
public interface IFollowsService extends IService<Follows> {
    Resp<String> followOthers(String account, String followerAccount);

    Resp<String> unFollowOthers(String userAccount, String followerAccount);

    Resp<FollowResp> getFolloweeList(String myAccount, String account, Integer pageNum, Integer pageSize);

    Resp<FollowResp> getFollowerList(String myAccount, String account, Integer pageNum, Integer pageSize);

    // 获取关注者和被关注者的数量（注意：这里都是对用户的视角来说的）
    Resp<FollowNumResp> getFolloweeAndFollowerCount(String account);

    // 获取 我关注了 列表
    Resp<FollowResp> getFollowerListV2(String account, Integer pageNum, Integer pageSize);

    // 获取 我被关注了 列表
    Resp<FollowResp> getFolloweeListV2(String account, Integer pageNum, Integer pageSize);

    // 获取 我关注了 列表V3(优化版本)
    Resp<FollowRespV3> getFollowerListV3(String myAccount, String account, Integer pageNum, Integer pageSize);

    // 获取 我被关注了 列表V3(优化版本)
    Resp<FollowRespV3> getFolloweeListV3(String myAccount, String account, Integer pageNum, Integer pageSize);


    // 获取 关注了 数量
    int countFollowersByAccount(String account);

    // 获取 被关注了 数量
    int countFolloweesByAccount(String account);


}
