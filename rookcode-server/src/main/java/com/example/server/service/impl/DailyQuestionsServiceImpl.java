package com.example.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.dto.Question.GetDailyQuestionResp;
import com.example.server.dto.Question.GetDailyQuestionsResp;
import com.example.server.dto.Resp;
import com.example.server.mapper.DailyQuestionsMapper;
import com.example.server.mapper.QuestionsMapper;
import com.example.server.mapper.RecordsMapper;
import com.example.server.mapper.UsersMapper;
import com.example.server.pojo.DailyQuestions;
import com.example.server.pojo.Questions;
import com.example.server.pojo.Records;
import com.example.server.pojo.Users;
import com.example.server.service.IDailyQuestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Elwin
 * @since 2023-04-24
 */
@Service
public class DailyQuestionsServiceImpl extends ServiceImpl<DailyQuestionsMapper, DailyQuestions> implements IDailyQuestionsService {
    public static final DailyQuestions DAILY_QUESTIONS = new DailyQuestions();
    @Autowired
    private DailyQuestionsMapper dailyQuestionsMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private QuestionsMapper questionsMapper;
    @Autowired
    private RecordsMapper recordsMapper;
    @Override
    public DailyQuestions generateDailyQuestion(LocalDate date) {
        // 生成随机问题ID
        int questionId = generateRandomQuestionId();

        // 查询今天的记录是否已经存在
        DailyQuestions dailyQuestions = dailyQuestionsMapper.findByDate(date);

        if (dailyQuestions == null) {
            // 如果记录不存在，插入一条新记录
            dailyQuestions = new DailyQuestions();
            dailyQuestions.setQuestionId(questionId);
            dailyQuestions.setDate(date);
            dailyQuestionsMapper.insert(dailyQuestions);
        } else {
            // 如果记录已经存在，更新对应的question_id
            dailyQuestions.setQuestionId(questionId);
            dailyQuestionsMapper.updateById(dailyQuestions);
        }

        return dailyQuestions;
    }

    /**
     * 获取每日一题信息
     * @param start_date
     * @param end_date
     * @return
     */
    @Override
    public Resp<GetDailyQuestionsResp> getDailyQuestions(LocalDate start_date, LocalDate end_date, String account) {
        // 获取用户id
        int user_id = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account)).getId();

        // 获取每日一题
        QueryWrapper<DailyQuestions> queryWrapper = new QueryWrapper<>();
        // 设置搜索条件
        queryWrapper.between("date", start_date, end_date);
        List<DailyQuestions> dailyQuestionsList = dailyQuestionsMapper.selectList(queryWrapper);

        // 创建返回对象
        List<GetDailyQuestionResp> questionResp = new ArrayList<>();
        GetDailyQuestionsResp resp = new GetDailyQuestionsResp();

        // 复制返回对象
        for (DailyQuestions dailyQuestion : dailyQuestionsList) {
            // 定义参数值
            LocalDate r_date = dailyQuestion.getDate();
            String r_prob_title = questionsMapper.selectById(dailyQuestion.getQuestionId()).getTitle();
            int r_prob_id = dailyQuestion.getQuestionId();
            boolean r_state = checkSolvedByDate(r_date, user_id, r_prob_id);

            GetDailyQuestionResp question = new GetDailyQuestionResp(r_date, r_prob_title, r_prob_id, r_state);
            questionResp.add(question);
        }

        // 返回对象内容填充
        resp.setQuestionRespList(questionResp);
        return Resp.success(resp);
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

    /**
     * 检查每日一题的完成情况
     * @param date
     * @param user_id
     * @param question_id
     * @return
     */
    private boolean checkSolvedByDate(LocalDate date, int user_id, int question_id) {
        // 设置查询条件
        QueryWrapper<Records> queryWrapper = new QueryWrapper<>();
        // 条件：在时间范围内（当天完成）
        queryWrapper.lambda().ge(Records::getSubmitDate, date.atStartOfDay())
                .lt(Records::getSubmitDate, date.plusDays(1).atStartOfDay());
        // 条件：是该用户完成的
        queryWrapper.eq("user_id", user_id);
        // 条件：是该题目
        queryWrapper.eq("question_id", question_id);
        // 获取符合条件的提交记录
        List<Records> recordsList = recordsMapper.selectList(queryWrapper);

        // 检查是否所有记录都是未通过
        boolean allStateIsZero = recordsList.stream().allMatch(record -> record.getState() == 0);

        return !allStateIsZero;
    }
}
