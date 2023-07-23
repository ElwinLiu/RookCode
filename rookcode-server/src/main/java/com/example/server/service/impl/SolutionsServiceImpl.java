package com.example.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.dto.*;
import com.example.server.dto.Comment.GetCommentResp;
import com.example.server.dto.Comment.GetCommentsResp;
import com.example.server.dto.Solution.*;
import com.example.server.mapper.*;
import com.example.server.pojo.*;
import com.example.server.service.IAnswerLikeService;
import com.example.server.service.ISolutionsService;
import com.example.server.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
@Service
public class SolutionsServiceImpl extends ServiceImpl<SolutionsMapper, Solutions> implements ISolutionsService {
    @Autowired(required = false)
    SolutionsMapper solutionsMapper;
    @Autowired(required = false)
    SolutionTagsMapper tagsMapper;
    @Autowired(required = false)
    SolutionTagsMapMapper solutionTagsMapMapper;
    @Autowired(required = false)
    IUsersService usersService;
    @Autowired(required = false)
    UsersMapper usersMapper;
    @Autowired(required = false)
    SolutionTagsMapper solutionTagsMapper;
    @Autowired(required = false)
    QuestionsMapper questionsMapper;
    @Autowired(required = false)
    CommentsMapper commentsMapper;

    @Autowired
    IAnswerLikeService answerLikeService;

    /**
     * // 用于发布题解
     *
     * @param questionId
     * @param title
     * @param content
     * @param tags
     * @param account
     * @return
     */
    @Override
    public Resp<Integer> publishSolution(int questionId, String title, String content, String tags, String account) {
        // 获取用户id
        int userId = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account)).getId();

        // 创建题解对象
        Solutions solution = new Solutions(questionId, userId, title, content, 0, 0, LocalDateTime.now());

        // 插入题解, 此时solution的id被赋值
        try {
            solutionsMapper.insert(solution);
        } catch (Exception e) {
            System.err.println("插入失败！异常信息为：" + e.getMessage());
            return Resp.fail("插入失败！");
        }

        int solutionId = solution.getId();

        // 插入tags
        if (inserTagsByIdANdTags(solutionId, tags) == 0)
            return Resp.fail("只可使用已经提供给您的标签！");

        return Resp.success(solution.getId());
    }

    /**
     * 根据组合条件搜索题解
     *
     * @param page
     * @param name
     * @param tags
     * @param question_id
     * @return
     */
    @Override
    public Resp<FindSolutionsResp> findSolutions(int page, String name, String tags, int question_id, String account) {
        // 创建分页对象，每页默认大小为10
        Page<Solutions> PG = new Page<>(page, 10);
        // 将tags处理为逗号分割
        String[] tempTags = tags.split("_");
        String tagString = Arrays.stream(tempTags).map(tag -> String.format("%s", tag)).collect(Collectors.joining(","));

        // 获取所有满足条件的题解
        List<Solutions> solutions = solutionsMapper.findSolutions(PG, tagString, name, question_id);

        // 获取所有满足条件的题解的个数
        int total_cnt = solutionsMapper.findSolutionsCnt(tagString, name, question_id);

        // 创建返回结果对象
        List<FindSolutionResp> resps = new ArrayList<>();
        FindSolutionsResp findSolutionsResp = new FindSolutionsResp();

        // 提取所有solution数据
        for (Solutions solution : solutions) {
            // 定义参数
            int r_id = solution.getId();
            String r_title = solution.getTitle();
            String r_description = solution.getContent().substring(0, Math.min(solution.getContent().length(), 100));
            String r_avatar = usersService.getUsersById(solution.getAuthorId()).getAvatar();
            int r_like_num = solution.getLikeNum();
            int r_view_num = solution.getViewNum();
            int r_authorId = solution.getAuthorId();
            int r_comments_cnt = 0;

            // 获取标签信息
            List<String> tmp_tags = solutionsMapper.findTagsBySolutionId(solution.getId());
            String r_tags = String.join("_", tmp_tags);

            // 获取题解的评论个数
            r_comments_cnt = commentsMapper.selectCount(new QueryWrapper<Comments>().eq("solution_id", solution.getId()));

            // 获取是否点赞
            boolean isLike = false;
            isLike = answerLikeService.isLikeAnswer(account, solution.getId());

            // 创建resp
            FindSolutionResp resp = new FindSolutionResp(r_id, r_title, r_description, r_avatar, r_like_num, r_view_num, r_tags, r_authorId, r_comments_cnt, isLike);

            // 添加至resps数组
            resps.add(resp);
        }

        // 写入返回结果对象
        findSolutionsResp.setTotal_cnt(total_cnt);
        findSolutionsResp.setFindSolutionRespList(resps);

        return Resp.success(findSolutionsResp);
    }

    /**
     * 根据题解id获取题解列表
     *
     * @param id
     * @return
     */
    @Override
    public Resp<GetSolutionResp> getSolution(String account, int id) {
        // 获取Solution数据
        Solutions solution = solutionsMapper.selectOne(new QueryWrapper<Solutions>().eq("id", id));
        if (solution == null) {
            return Resp.fail("不存在该id的题解");
        }

        // 获取Solution数据对应的标签
        List<String> tmp_tags = solutionsMapper.findTagsBySolutionId(solution.getId());
        String r_tags = String.join("_", tmp_tags);

        // 定义参数
        Users userTmp = usersService.getUsersById(solution.getAuthorId());
        String r_title = solution.getTitle();
        String r_content = solution.getContent();
        String r_avatar = userTmp.getAvatar();
        int r_like_num = solution.getLikeNum();
        int r_view_num = solution.getViewNum();
        int r_authorId = solution.getAuthorId();
        String r_nickname = userTmp.getNickname();
        String r_account = userTmp.getAccount();
        LocalDateTime r_dateTime = solution.getSubmitDate();

        // 搜索一次算一次浏览
        solutionsMapper.increaseViewNumById(solution.getId());


        // 判断用户是否点赞过当前题解
        boolean isLike = false;
        isLike = answerLikeService.isLikeAnswer(account, solution.getId());

        // 将Solution数据复制到GetSolutionResp中
        GetSolutionResp resp = new GetSolutionResp(r_title, r_content,
                r_avatar, r_like_num, r_view_num + 1, r_tags,
                r_authorId, r_nickname, r_dateTime, r_account, isLike);


        return Resp.success(resp);
    }

    /**
     * 根据用户的账号和页面，获取题解
     *
     * @param page
     * @param account
     * @return
     */
    @Override
    public Resp<GetSolutionsByUserResp> getSolutionsByUser(int page, String account) {
        // 创建分页对象，每页默认大小为10
        Page<Solutions> PG = new Page<>(page, 15);

        // 根据用户账号获取用户的id
        Users user = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account));
        if (user == null) {
            // 若查无此用户
            return Resp.fail("不存在该用户");
        }
        int userId = user.getId();

        // 根据作者的id获取题解列表
        List<Solutions> solutions = solutionsMapper.selectPage(PG, new QueryWrapper<Solutions>().eq("author_id", userId).orderByDesc("submit_date")).getRecords();

        // 创建题解列表
        List<GetSolutionByUserResp> solutionResps = new ArrayList<>();

        // 复制题解列表
        for (Solutions solution : solutions) {
            // 定义目标参数
            int r_id = solution.getId();
            String r_question_title = questionsMapper.selectOne(new QueryWrapper<Questions>().eq("id", solution.getQuestionId())).getTitle();
            String r_solution_title = solution.getTitle();
            int r_view = solution.getViewNum();
            int r_like = solution.getLikeNum();
            int r_question_id = solution.getQuestionId();
            LocalDateTime r_date = solution.getSubmitDate();

            // 创建需求对象并插入List
            GetSolutionByUserResp solutionResp = new GetSolutionByUserResp(r_id, r_question_title, r_solution_title, r_view, r_like, r_date, r_question_id);
            solutionResps.add(solutionResp);
        }

        // 获取页数
        int count = solutionsMapper.selectCount(new QueryWrapper<Solutions>().eq("author_id", userId)); // 一共有多少条
        int r_total_page = (int) Math.ceil((double) count / 15); // 向上取整获取页数

        // 创建返回对象
        GetSolutionsByUserResp resp = new GetSolutionsByUserResp(solutionResps, r_total_page);

        return Resp.success(resp);
    }

    /**
     * 根据题解id删除题解
     *
     * @param id
     * @param account
     * @return
     */
    @Override
    public Resp<String> deleteSolution(int id, String account) {
        // 获取题解的作者id
        QueryWrapper<Solutions> queryWrapper = new QueryWrapper<Solutions>().eq("id", id);
        Solutions solution = solutionsMapper.selectOne(queryWrapper);
        int authorId = solution.getAuthorId();

        // 检查是否为本人操作
        int check = usersService.checkAuth(authorId, account);
        if (check == 0) {
            return Resp.fail("您的用户ID异常");
        } else if (check == 1) {
            return Resp.fail("没有权限控制其他用户");
        }

        try {
            solutionsMapper.delete(queryWrapper);
        } catch (Exception e) {
            System.err.println("删除失败！异常信息为：" + e.getMessage());
            return Resp.fail("删除失败！");
        }
        return Resp.success("成功删除id为" + id + "的题解");
    }

    /**
     * // 获取标签表单中的所有标签名称，用下划线_连接
     *
     * @return
     */
    @Override
    public Resp<String> getAllTags() {
        // 查询标签表单
        List<SolutionTags> solutionTags = solutionTagsMapper.selectList(new QueryWrapper<SolutionTags>());
        // 将所有tag的名称取出，存入tagNames
        List<String> tagNames = solutionTags.stream()
                .map(SolutionTags::getName)
                .collect(Collectors.toList());
        // 用_符号连接所有tag
        String joinedTagNames = String.join("_", tagNames);
        return Resp.success(joinedTagNames);
    }

    // 更新solution
    @Override
    public Resp<UpdateSolutionResp> updateSolution(String account, int solution_id, String title, String content, String tags) {
        // 根据用户账号获取用户的id
        Users user = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account));
        if (user == null) {
            // 若查无此用户
            return Resp.fail("不存在该用户");
        }
        int user_id = user.getId();

        // 获取旧版本Solution
/*        Solutions solution_old = solutionsMapper.selectById(solution_id);
        if (solution_old == null) return Resp.fail("不存在该id的题解！");*/

        // 查询Solution
        Solutions solution = solutionsMapper.selectOne(new QueryWrapper<Solutions>().eq("id", solution_id));
        if(solution== null) return Resp.fail("不存在该id的题解！");

/*        // 创建新版本solution
        Solutions solution_new = new Solutions(solution_old.getId(), solution_old.getQuestionId(), solution_old.getAuthorId(),
                title, content, solution_old.getLikeNum(),
                solution_old.getViewNum(), solution_old.getSubmitDate(), solution_old.getVersion());*/

        // 更新题解内容
        solution.setTitle(title);
        solution.setContent(content);

        // 插入题解, 此时solution_new的id被赋值
        try {
            // solutionsMapper.updateById(solution_new);
            // 更新题解
            solutionsMapper.update(solution, new UpdateWrapper<Solutions>().eq("id", solution_id));
        } catch (Exception e) {
            System.err.println("插入失败！异常信息为：" + e.getMessage());
            return Resp.fail("插入失败！");
        }

        // 将solution_new的标签插入表单
/*        if (inserTagsByIdANdTags(solution_new.getId(), tags) == 0)
            return Resp.fail("只可使用已经提供给您的标签！");*/
        // 更新题解标签
        if (inserTagsByIdANdTags(solution.getId(), tags) == 0)
            return Resp.fail("只可使用已经提供给您的标签！");

        // return Resp.success(new UpdateSolutionResp(solution_new.getId()));
        // 返回更新后的题解id
        return Resp.success(new UpdateSolutionResp(solution.getId()));
    }

    @Override
    public Resp<String> publishComment(String account, int solution_id, String content) {
        // 根据用户账号获取用户的id
        Users user = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account));
        if (user == null) {
            // 若查无此用户
            return Resp.fail("不存在该用户");
        }
        int user_id = user.getId();

        // 创建待插入对象
        LocalDateTime now = LocalDateTime.now();
        Comments comments = new Comments(null, solution_id, user_id, now, content);

        // 执行插入操作
        try {
            commentsMapper.insert(comments);
        } catch (Exception e) {
            Resp.fail("插入异常！");
        }

        return Resp.success("插入成功！时间：" + now);
    }

    @Override
    public Resp<GetCommentsResp> getComments(int page, int solution_id) {
        // 创建分页对象，每页默认大小为10
        Page<Comments> PG = new Page<>(page, 10);

        // 获取该题解下的所有评论对象
        List<Comments> commentsList = commentsMapper.selectPage(PG, new QueryWrapper<Comments>().eq("solution_id", solution_id).orderByDesc("submit_date")).getRecords();

        // 创建评论列表
        List<GetCommentResp> commentRespList = new ArrayList<>();
        // 复制评论列表
        for (Comments comment : commentsList) {
            // 创建需要的参数
            int r_comment_id = comment.getId();
            int r_user_id = comment.getPublisherId();
            LocalDateTime r_datetime = comment.getSubmitDate();
            String r_content = comment.getContent();
            String r_nickname, r_avatar, r_account;

            // 获取发布评论的用户数据
            Users user = usersMapper.selectById(r_user_id);
            // 给昵称、头像和账号赋值
            r_nickname = user.getNickname();
            r_avatar = user.getAvatar();
            r_account = user.getAccount();

            // 加入评论列表
            commentRespList.add(new GetCommentResp(r_comment_id, r_user_id,
                    r_nickname, r_avatar, r_datetime, r_content, r_account));
        }

        // 获取总页数
        int r_total_page = (int) Math.ceil((double) commentsMapper.selectCount(new QueryWrapper<Comments>().eq("solution_id", solution_id)) / 10);
        ;
        GetCommentsResp resp = new GetCommentsResp(r_total_page, commentRespList);

        return Resp.success(resp);
    }

    @Override
    public Resp<String> adminDeleteSolution(int id, String account) {
        // 检查是否为管理员
        if (!usersService.is_admin(account)) return Resp.fail("请求失败，您不是管理员！");

        // 删除目标题解
        try {
            solutionsMapper.delete(new QueryWrapper<Solutions>().eq("id", id));
        }
        // 如果删除失败
        catch (Exception e) {
            System.err.println("删除失败！异常信息为：" + e.getMessage());
            return Resp.fail("删除失败！");
        }

        // 返回提示信息
        return Resp.success("成功删除id为" + id + "的题解");
    }

    // 根据tags字符串和题解id插入solutionTagsMap表单,并删除旧的tags
    private int inserTagsByIdANdTags(int solutionId, String tags) {
        // 将旧的solution_tags删除
        solutionTagsMapMapper.delete(new QueryWrapper<SolutionTagsMap>().eq("solution_id", solutionId));

        // 插入tags
        // 将“C++_动态规划_数组”类的标签字符串做切分
        String[] tagList = tags.split("_");
        try {
            // 查询标签id并插入映射表单
            for (String tag : tagList) {
                if (tag.isEmpty()) continue;
                // 查询对应的tag元组
                SolutionTags solutionTag = tagsMapper.selectOne(new QueryWrapper<SolutionTags>().eq("name", tag));
                // 获取该tag的id
                int tagId = solutionTag.getId();
                // 创建题解-标签对象
                SolutionTagsMap solutionTagsMap = new SolutionTagsMap();
                solutionTagsMap.setSolutionId(solutionId);
                solutionTagsMap.setTag(tagId);
                // 插入题解-标签表单
                solutionTagsMapMapper.insert(solutionTagsMap);
            }
        } catch (NullPointerException e) {
            // 报错：只可使用已经提供给您的标签！
            return 0;
        }
        // 成功插入
        return 1;
    }
}
