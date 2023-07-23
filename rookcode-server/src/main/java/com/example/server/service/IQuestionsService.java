package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.dto.*;
import com.example.server.dto.Question.*;
import com.example.server.dto.User.ProgressResp;
import com.example.server.pojo.Questions;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
public interface IQuestionsService extends IService<Questions> {
    // 根据页数获取题目
    Resp<List<QuestionResp>> getQuestions(int page);
    // 根据id获取题目
    Resp<QuestionResp> getQuestion(int id);

    /**
     * 多条件查询
     * @param account 用户名
     * @param difficulty 题解难度
     * @param state 题解状态(none, unanswered, solved, tried)
     * @param tags 标签
     * @param input 输入，模糊查询
     * @param page 页面
     * @return ShowProblemListResp
     */
    Resp<ShowProblemListResp> showProblemList(String account, String difficulty, String state, List<String> tags, String input, int page);

    /**
     * 获取进度
     * @param account 用户名
     * @return 用户做题进度
     */
    Resp<ProgressResp> getProgress(String account);

    /**
     * 获取题目的通过情况
     * @param id 题目的id
     * @return 返回题目的通过情况
     */
    Resp<QuestionInfoResp> getQuestionInfo(int id);

    // 发布题目
    Resp<Integer> publishQuestion(QuestionTagsDTO questionTagsDTO);

    // 修改题目
    Resp<String> updateQuestion(QuestionTagsDTO questionTagsDTO);

    // 删除题目
    Resp<String> deleteQuestion(Integer id);

    Resp<String> updateQuestionDifficulty(UpdateDifficultyDTO updateDifficultyDTO);

    Resp<String> updateQuestionTags(UpdateTagsRequest updateTagsRequest);
}
