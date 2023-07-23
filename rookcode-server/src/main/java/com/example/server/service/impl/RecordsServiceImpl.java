package com.example.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.SelectList;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.dto.Resp;
import com.example.server.mapper.LanguagesMapper;
import com.example.server.mapper.QuestionsMapper;
import com.example.server.mapper.RecordsMapper;
import com.example.server.mapper.UsersMapper;
import com.example.server.pojo.Questions;
import com.example.server.pojo.Records;
import com.example.server.pojo.Users;
import com.example.server.service.IRecordsService;
import com.example.server.vo.RecordsDetailVO;
import com.example.server.vo.RecordsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
@Service
public class RecordsServiceImpl extends ServiceImpl<RecordsMapper, Records> implements IRecordsService {
    @Autowired(required = false)
    RecordsMapper recordsMapper;

    @Autowired(required = false)
    LanguagesMapper languagesMapper;

    @Autowired(required = false)
    QuestionsMapper questionsMapper;

    @Autowired(required = false)
    UsersMapper usersMapper;

    /*
     * @Author YFMan
     * @Description 得到某个用户某道题目的提交记录
     * @Date 2023/4/26 0:01
     * @Param [userId, questionId]
     * @return com.example.server.dto.Resp<java.util.List<com.example.server.pojo.Records>>
     **/
    @Override
    public Resp<List<RecordsVO>> getRecordsList(String account, Integer questionId) {
        // 构造查询条件
        QueryWrapper<Users> queryUserWrapper = new QueryWrapper<>();
        queryUserWrapper.lambda().eq(Users::getAccount, account);

        // 执行查询操作
        Users user = usersMapper.selectOne(queryUserWrapper);

        // 获取查询结果的id字段
        Integer userId = user.getId();

        // 书写 sql 语句
        QueryWrapper<Records> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("question_id", questionId);
        queryWrapper.orderByDesc("submit_date");

        // 得到返回列表
        List<Records> recordsList = recordsMapper.selectList(queryWrapper);
        List<RecordsVO> recordsVOList = convertRecordsToRecordsVO(recordsList);
        return Resp.success(recordsVOList);
    }

    /*
     * @Author YFMan
     * @Description 根据记录的 id 获取提交记录的详细信息
     * @Date 2023/4/26 12:06
     * @Param [recordsId]
     * @return com.example.server.dto.Resp<com.example.server.vo.RecordsDetailVO>
     **/
    @Override
    public Resp<RecordsDetailVO> getRecordsDetail(Integer recordsId) {
        Records records = recordsMapper.selectById(recordsId);
        // 创建 recordsDetail view object
        RecordsDetailVO recordsDetailVO = new RecordsDetailVO();
        // 设置相应的值
        recordsDetailVO.setExtraInfo(records.getLog());
        recordsDetailVO.setQuestionId(records.getQuestionId());
        recordsDetailVO.setSubmitCode(records.getSubmitCode());
        recordsDetailVO.setLangName(languagesMapper.selectLanguageNameById(records.getLangId()));
        recordsDetailVO.setTestCaseAccessNum(records.getTestCaseAccessNum());
        recordsDetailVO.setTestCaseTotalNum(records.getTestCaseTotalNum());
        recordsDetailVO.setMemory(records.getMemory());
        recordsDetailVO.setResult(records.getResult());
        recordsDetailVO.setExecTime(records.getExecTime());
        recordsDetailVO.setSubmitDate(records.getSubmitDate());

        // 根据题目 id 获取题目 title
        recordsDetailVO.setQuestionTitle(getQuestionTitleById(records.getQuestionId()));

        // 返回给前端进行渲染
        return Resp.success(recordsDetailVO);
    }

    private String getQuestionTitleById(Integer id) {
        Questions questions = questionsMapper.selectById(id);
        return questions.getTitle();
    }


    @Override
    public List<RecordsVO> convertRecordsToRecordsVO(List<Records> recordsList) {
        List<RecordsVO> recordsVOList = new ArrayList<>();
        for (Records records : recordsList) {
            RecordsVO recordsVO = new RecordsVO();
            recordsVO.setId(records.getId());
            recordsVO.setExecTime(records.getExecTime());
            recordsVO.setMemory(records.getMemory());
            recordsVO.setResult(records.getResult());
            recordsVO.setLangName(languagesMapper.selectLanguageNameById(records.getLangId()));
            recordsVO.setSubmitDate(records.getSubmitDate());
            recordsVOList.add(recordsVO);
        }
        return recordsVOList;
    }
}
