package com.example.server.util;

import com.example.server.mapper.DailyQuestionsMapper;
import com.example.server.mapper.QuestionsMapper;
import com.example.server.pojo.DailyQuestions;
import com.example.server.pojo.Questions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class DailyQuestionScheduler {
    @Autowired
    private DailyQuestionsMapper dailyQuestionsMapper;

    @Autowired
    private QuestionsMapper questionsMapper;

    @Scheduled(cron = "0 0 0 * * ?") // 每天零点执行一次
    public DailyQuestions generateDailyQuestion() {
        // 生成随机问题ID
        int questionId = generateRandomQuestionId();

        // 查询今天的记录是否已经存在
        LocalDate today = LocalDate.now();
        DailyQuestions dailyQuestions = dailyQuestionsMapper.findByDate(today);

        if (dailyQuestions == null) {
            // 如果记录不存在，插入一条新记录
            dailyQuestions = new DailyQuestions();
            dailyQuestions.setQuestionId(questionId);
            dailyQuestions.setDate(today);
            dailyQuestionsMapper.insert(dailyQuestions);
        } else {
            // 如果记录已经存在，更新对应的question_id
            dailyQuestions.setQuestionId(questionId);
            dailyQuestionsMapper.updateById(dailyQuestions);
        }

        return dailyQuestions;
    }

    private int generateRandomQuestionId() {
        // 查询问题表中的所有问题ID
        List<Integer> questionIds = questionsMapper.selectList(null)
                .stream()
                .map(Questions::getId)
                .collect(Collectors.toList());

        // 随机选择一个问题ID
        Random random = new Random();
        int index = random.nextInt(questionIds.size());
        return questionIds.get(index);
    }
}
