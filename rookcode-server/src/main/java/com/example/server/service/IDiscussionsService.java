package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.dto.Discussion.GetCommentsResp;
import com.example.server.dto.Discussion.GetDiscussionsResp;
import com.example.server.dto.Discussion.GetDiscussionByIdResp;
import com.example.server.dto.Resp;
import com.example.server.pojo.Discussions;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Elwin
 * @since 2023-05-05
 */
public interface IDiscussionsService extends IService<Discussions> {
    /**
     * 获取讨论列表
     * @param page 请求页数
     * @return 讨论列表
     */
    Resp<GetDiscussionsResp> getDiscussions(int page, String account);

    /**
     * 获取某个用户发布的话题
     * @param page 页数
     * @param account 用户账号
     * @return 返回列表
     */
    Resp<GetDiscussionsResp> getDiscussionsByUser(int page, String account);

    /**
     * 组合条件搜索话题
     * @param page 页数
     * @param account 用户账号
     * @param title 标题
     * @return 话题列表
     */
    Resp<GetDiscussionsResp> findDiscussions(int page, String account, String title);

    Resp<GetDiscussionByIdResp> getDiscussionById(int discussion_id, String account);

    /**
     * 发布讨论
     * @param title 标题
     * @param content 内容
     * @param account 账号
     * @return 新发布的讨论id
     */
    Resp<Integer> pubDiscussion(String title, String content, String account);

    /**
     * 评论话题
     * @param discussion_id 被评论的话题id
     * @param account 用户账号
     * @return 评论的id
     */
    Resp<Integer> commentDiscussion(int discussion_id,String content, String account);

    /**
     * 获取话题的评论列表（每列10条）
     * @param page 评论的页数
     * @param discussion_id 话题的id
     * @return 10条评论
     */
    Resp<GetCommentsResp> getComments(int page, int discussion_id);

    /**
     * 给讨论区的话题点赞
     * @param discussion_id 话题的id
     * @param account 用户账号
     * @return 返回点赞状态
     */
    Resp<Boolean> like(int discussion_id, String account);

    /**
     * 管理员删除讨论区的话题
     * @param id 话题的id
     * @param account 账户
     * @return 删除结果，是否成功删除
     */
    Resp<String> adminDeleteDiscussion(int id, String account);

    /**
     * 用户删除讨论区的话题
     * @param id 话题的id
     * @param account 账户
     * @return 删除结果，是否成功删除
     */
    Resp<String> DeleteDiscussion(int id, String account);

    /**
     * 编辑话题
     * @param id 话题的id
     * @param title 话题的新标题
     * @param content 话题的内容
     * @param account 编辑者的账户
     * @return 结果消息
     */
    Resp<String> updateDiscussion(int id, String title, String content, String account);

}
