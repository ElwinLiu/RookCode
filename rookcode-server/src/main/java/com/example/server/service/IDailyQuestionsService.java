package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.dto.Question.GetDailyQuestionsResp;
import com.example.server.dto.Resp;
import com.example.server.pojo.DailyQuestions;

import java.time.LocalDate;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Elwin
 * @since 2023-04-24
 */
public interface IDailyQuestionsService extends IService<DailyQuestions> {
    /**
     * 根据日期随机插入/更新当天的每日一题
     * @param date
     * @return
     */
    DailyQuestions generateDailyQuestion(LocalDate date);

    /**
     * 获取每日一题信息
     * @param start_date
     * @param end_date
     * @return
     */
    Resp<GetDailyQuestionsResp> getDailyQuestions(LocalDate start_date, LocalDate end_date, String account);
}
