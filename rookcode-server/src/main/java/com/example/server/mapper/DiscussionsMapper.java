package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.server.dto.Discussion.GetCommentResp;
import com.example.server.dto.Discussion.GetDiscussionResp;
import com.example.server.dto.Discussion.GetDiscussionByIdResp;
import com.example.server.pojo.Discussions;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Elwin
 * @since 2023-05-05
 */
public interface DiscussionsMapper extends BaseMapper<Discussions> {
    /**
     * 根据页数获取话题列表
     * @param page 页数
     * @param userId 用户id
     * @return 返回话题列表
     */
    List<GetDiscussionResp> getDiscussions(Page page, @Param("userId")Integer userId);

    /**
     * 获取某个用户发布的话题
     * @param page 页数
     * @param userId 用户id
     * @return 列表
     */
    List<GetDiscussionResp> getDiscussionsByUser(Page page, @Param("userId")Integer userId);

    /**
     * 根据组合条件获取话题列表
     * @param page 页数
     * @param userId 用户id
     * @param title 标题（模糊查询）
     * @return 返回话题列表
     */
    List<GetDiscussionResp> findDiscussions(Page page, @Param("userId")Integer userId, @Param("title")String title);

    /**
     * 根据id获取话题详情
     * @param userId 查看话题的用户id
     * @param discussionId 话题的id
     * @return getDiscussionByIdResp对象
     */
    GetDiscussionByIdResp getDiscussionById(@Param("userId")Integer userId, @Param("discussionId")Integer discussionId);

    List<GetCommentResp> getComments(Page page, @Param("discussionId")Integer discussionId);

    @Delete("DELETE FROM discussion_like WHERE discussion_id = #{discussionId} AND user_id = #{userId}")
    int deleteByDiscussionIdAndUserId(@Param("discussionId") Integer discussionId, @Param("userId") Integer userId);
}
