package com.example.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.dto.Discussion.*;
import com.example.server.dto.Resp;
import com.example.server.mapper.DiscussionCommentsMapper;
import com.example.server.mapper.DiscussionLikeMapper;
import com.example.server.mapper.DiscussionsMapper;
import com.example.server.mapper.UsersMapper;
import com.example.server.pojo.*;
import com.example.server.service.IDiscussionsService;
import com.example.server.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Elwin
 * @since 2023-05-05
 */
@Service
public class DiscussionsServiceImpl extends ServiceImpl<DiscussionsMapper, Discussions> implements IDiscussionsService {
    @Autowired
    UsersMapper usersMapper;
    @Autowired
    DiscussionLikeMapper discussionLikeMapper;
    @Autowired
    DiscussionsMapper discussionsMapper;
    @Autowired
    DiscussionCommentsMapper discussionCommentsMapper;
    @Autowired
    IUsersService usersService;

    @Override
    public Resp<GetDiscussionsResp> getDiscussions(int page, String account) {
        // 获取用户id
        int userId = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account)).getId();

        // 定义每页的大小
        int page_size = 15;
        // 创建分页对象
        Page<GetDiscussionResp> PG = new Page<>(page, page_size);

        // 获取讨论列表
        List<GetDiscussionResp> discussionList = discussionsMapper.getDiscussions(PG, userId);

        // 创建返回参数
        GetDiscussionsResp resp = new GetDiscussionsResp(discussionList, discussionsMapper.selectCount(new QueryWrapper<>()));

        return Resp.success(resp);
    }

    /**
     * 获取某个用户发布的话题
     * @param page 页数
     * @param account 用户账号
     * @return 话题列表
     */
    @Override
    public Resp<GetDiscussionsResp> getDiscussionsByUser(int page, String account) {
        // 获取用户id
        int userId = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account)).getId();

        // 定义每页的大小
        int page_size = 15;
        // 创建分页对象
        Page<GetDiscussionResp> PG = new Page<>(page, page_size);

        // 获取讨论列表
        List<GetDiscussionResp> discussionList = discussionsMapper.getDiscussionsByUser(PG, userId);

        // 创建返回参数
        GetDiscussionsResp resp = new GetDiscussionsResp(discussionList, (int)PG.getPages());

        return Resp.success(resp);

    }

    @Override
    public Resp<GetDiscussionsResp> findDiscussions(int page, String account, String title) {
        // 获取用户id
        int userId = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account)).getId();

        // 定义每页的大小
        int page_size = 15;
        // 创建分页对象
        Page<GetDiscussionResp> PG = new Page<>(page, page_size);

        // 获取讨论列表
        List<GetDiscussionResp> discussionList = discussionsMapper.findDiscussions(PG, userId, title);

        // 创建返回参数
        GetDiscussionsResp resp = new GetDiscussionsResp(discussionList, (int)PG.getPages());

        return Resp.success(resp);
    }

    @Override
    public Resp<GetDiscussionByIdResp> getDiscussionById(int discussion_id, String account) {
        // 获取用户id
        int userId = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account)).getId();

        // 获取对象
        GetDiscussionByIdResp resp = discussionsMapper.getDiscussionById(userId, discussion_id);
        if (resp == null) {
            Resp.fail("查询失败，请联系技术人员！");
        }

        // 目标话题的浏览量自增1
        UpdateWrapper<Discussions> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", discussion_id)
                .setSql("view_num = view_num + 1");

        discussionsMapper.update(null, updateWrapper);

        return Resp.success(resp);
    }

    @Override
    public Resp<Integer> pubDiscussion(String title, String content, String account) {
        // 获取用户id
        int userId = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account)).getId();

        // 创建待插入对象
        Discussions discussions = new Discussions(null, userId, title, content, 0, LocalDateTime.now());

        // 插入数据库
        try {
            discussionsMapper.insert(discussions);
        }
        catch (Exception e) {
            System.err.println("插入讨论失败！异常信息为：" + e.getMessage());
            return Resp.fail("发布失败！");
        }

        return Resp.success(discussions.getId());
    }

    @Override
    public Resp<Integer> commentDiscussion(int discussion_id,String content, String account) {
        // 获取用户id
        int userId = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account)).getId();

        // 创建待插入对象
        DiscussionComments comment = new DiscussionComments(null, discussion_id, userId, LocalDateTime.now(), content);

        // 插入数据库
        try {
            discussionCommentsMapper.insert(comment);
        }
        catch (Exception e) {
            System.err.println("插入评论失败！异常信息为：" + e.getMessage());
            return Resp.fail("发布失败！");
        }

        return Resp.success(comment.getId());
    }

    @Override
    public Resp<GetCommentsResp> getComments(int page, int discussion_id) {
        // 定义每页的大小
        int page_size = 10;
        // 创建分页对象
        Page<GetCommentResp> PG = new Page<>(page, page_size);

        // 获取评论列表
        List<GetCommentResp> commentList = null;
        try {
            commentList = discussionsMapper.getComments(PG, discussion_id);
        }
        catch (Exception e) {
            System.err.println("获取评论失败！异常信息为：" + e.getMessage());
            return Resp.fail("评论获取失败！");
        }

        // 创建返回参数
        GetCommentsResp resp = new GetCommentsResp((int)PG.getPages(), commentList);

        return Resp.success(resp);
    }

    @Override
    public Resp<Boolean> like(int discussion_id, String account) {
        // 获取用户id
        int userId = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account)).getId();

        // 插入点赞数据
        try {
            discussionLikeMapper.insert(new DiscussionLike(discussion_id, userId));
        }
        // 如果插入失败，原因是已经点过赞，故删除之前的记录即可
        catch (DuplicateKeyException e) {
            discussionsMapper.deleteByDiscussionIdAndUserId(discussion_id, userId);
            return Resp.success(false);
        }

        return Resp.success(true);
    }

    @Override
    public Resp<String> DeleteDiscussion(int id, String account) {
        // 获取用户id
        int userId = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account)).getId();

        if (discussionsMapper.selectById(id).getUserId() != userId) return Resp.fail("您不可以删除其他用户发布的话题");

        // 删除目标题解
        try {
            discussionsMapper.delete(new QueryWrapper<Discussions>().eq("id", id));
        }
        // 如果删除失败
        catch (Exception e) {
            System.err.println("删除失败！异常信息为：" + e.getMessage());
            return Resp.fail("删除失败！");
        }

        // 返回提示信息
        return Resp.success("成功删除id为" + id + "的话题");
    }

    @Override
    public Resp<String> adminDeleteDiscussion(int id, String account) {
        // 检查是否为管理员
        if (!usersService.is_admin(account)) return Resp.fail("请求失败，您不是管理员！");

        // 删除目标题解
        try {
            discussionsMapper.delete(new QueryWrapper<Discussions>().eq("id", id));
        }
        // 如果删除失败
        catch (Exception e) {
            System.err.println("删除失败！异常信息为：" + e.getMessage());
            return Resp.fail("删除失败！");
        }

        // 返回提示信息
        return Resp.success("成功删除id为" + id + "的话题");
    }

    @Override
    public Resp<String> updateDiscussion(int id, String title, String content, String account) {
        // 获取用户id
        int userId = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account)).getId();

        // 获取旧的discussion对象
        Discussions discussion = discussionsMapper.selectOne(new QueryWrapper<Discussions>().eq("id", id));
        if (discussion == null) {
            return Resp.fail("不存在该id的话题");
        }
        // 检查是否为该话题的作者
        if (discussion.getUserId() != userId) {
            // 若不是
            return Resp.fail("没有权限修改他人话题内容");
        }

        // 修改话题
        discussion.setContent(content);
        discussion.setTitle(title);

        // 更新操作
        try {
            discussionsMapper.updateById(discussion);
        }
        catch (Exception e) {
            System.err.println("更新异常，日志：" + e.getMessage());
            return Resp.fail("更新失败，请联系技术人员");
        }

        return Resp.success("更新成功！");
    }
}
