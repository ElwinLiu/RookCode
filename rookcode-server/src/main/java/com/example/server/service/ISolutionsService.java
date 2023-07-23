package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.dto.*;
import com.example.server.dto.Comment.GetCommentsResp;
import com.example.server.dto.Solution.*;
import com.example.server.pojo.Solutions;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
public interface ISolutionsService extends IService<Solutions> {
    // 发布题解
    Resp<Integer> publishSolution(int questionId,
                                  String title, String content, String tags, String account);

    // 根据组合条件查找题解
    Resp<FindSolutionsResp> findSolutions(int page, String name, String tags, int question_id,String account);

    // 根据题解的id获取题解
    Resp<GetSolutionResp> getSolution(String account, int id);

    // 根据用户的id获取题解
    Resp<GetSolutionsByUserResp> getSolutionsByUser(int page, String account);

    // 根据题解的id删除题解
    Resp<String> deleteSolution(int id, String account);

    // 获取所有标签
    Resp<String> getAllTags();

    // 更新solution
    Resp<UpdateSolutionResp> updateSolution(String account, int solution_id, String title, String content, String tags);

    /**
     * 发布对题解的评论
     * @param account 用户的账号
     * @param solution_id 所评论题解的id
     * @param content 题解的内容
     * @return 返回成功/失败标识
     */
    Resp<String> publishComment(String account, int solution_id, String content);

    /**
     * 获取题解的评论
     * @param page 获取的评论页数
     * @param solution_id 题解的id
     * @return 评论列表和总页数
     */
    Resp<GetCommentsResp> getComments(int page, int solution_id);

    /**
     * 管理员根据题解id删除题解
     * @param id 题解的id
     * @param account 管理员的账户
     * @return 返回成功/失败标识
     */
    Resp<String> adminDeleteSolution(int id, String account);
}
