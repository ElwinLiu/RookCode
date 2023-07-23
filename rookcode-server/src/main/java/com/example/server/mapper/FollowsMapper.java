package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.dto.Follow.FollowEntity;
import com.example.server.dto.Follow.FollowEntityV3;
import com.example.server.pojo.Follows;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author YFMan
 * @Description 关注表的 Mapper 接口
 * @Date 2023/5/4 18:04
 */
public interface FollowsMapper extends BaseMapper<Follows> {
    // 根据 account 获取 关注了 数量的函数
    int countFollowersByAccount(String account);

    // 根据 account 获取 被关注了 数量的函数
    int countFolloweesByAccount(String account);

    // 根据 account 获取 指定位置 和 数量 的 我关注了 列表
    List<FollowEntity> getFollowersByAccount(@Param("account") String account, @Param("start") Long start, @Param("count") Integer count);

    // 根据 account 获取 指定位置 和 数量 的 我被关注了 列表
    List<FollowEntity> getFolloweesByAccount(@Param("account") String account, @Param("start") Long start, @Param("count") Integer count);

    // 根据 account 获取 指定位置 和 数量 的 我关注了 列表V3(优化版本)
    List<FollowEntityV3> getFollowersByAccountV3(@Param("myAccount") String myAccount, @Param("account") String account,
                                                 @Param("start") Long start, @Param("count") Integer count);

    // 根据 account 获取 指定位置 和 数量 的 我被关注了 列表V3(优化版本)
    List<FollowEntityV3> getFolloweesByAccountV3(@Param("myAccount") String myAccount, @Param("account") String account,
                                               @Param("start") Long start, @Param("count") Integer count);
}
