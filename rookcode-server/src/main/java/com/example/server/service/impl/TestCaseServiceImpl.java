package com.example.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.dto.Resp;
import com.example.server.dto.TestCase.TestCasesResp;
import com.example.server.dto.Judgement.TestCaseResp;
import com.example.server.mapper.QuestionsMapper;
import com.example.server.mapper.TestCaseMapper;
import com.example.server.pojo.Questions;
import com.example.server.pojo.TestCase;
import com.example.server.service.IOssFileService;
import com.example.server.service.ITestCaseService;
import com.example.server.vo.TestCaseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YFMan
 * @Description ITestCaseService 实现类
 * @Date 2023/4/22 15:35
 */
@Service
public class TestCaseServiceImpl extends ServiceImpl<TestCaseMapper, TestCase> implements ITestCaseService {

    @Autowired(required = false)
    IOssFileService ossFileService;
    @Autowired(required = false)
    QuestionsMapper questionsMapper;

    @Autowired(required = false)
    TestCaseMapper testCaseMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Resp<String> publishTestCase(TestCaseResp testCaseResp) {
        /*
         * 这里要检查是否具有管理员权限
         *
         **/
        // 检查question_id是否存在
        Questions questions = questionsMapper.selectById(testCaseResp.getQuestionId());
        if (questions == null) return Resp.fail("question_id不存在！");

        // 上传到 oss 并获取 url
        String inputPath = ossFileService.uploadFile(testCaseResp.getInput());
        String outputPath = ossFileService.uploadFile(testCaseResp.getExpectOutput());

        // 建立 TestCase 对象
        TestCase testCase = new TestCase(testCaseResp.getId(), testCaseResp.getQuestionId(), inputPath, outputPath);
        // boolean isSuccess = save(testCase);

        // 存储测试用例，返回 id
        Integer count = testCaseMapper.saveTestCaseReturnId(testCase);
        if (count != null) {
            return Resp.success(testCase.getId().toString());
        } else {
            return Resp.fail("发布失败！");
        }
    }

    @Override
    public Resp<String> updateTestCase(TestCaseResp testCaseResp) {
        /*
         * 这里要检查是否具有管理员权限
         *
         **/
        // 检查question_id是否存在
        Questions questions = questionsMapper.selectById(testCaseResp.getQuestionId());
        if (questions == null) return Resp.fail("question_id不存在！");

        // 上传到 oss 并获取 url
        String inputPath = ossFileService.uploadFile(testCaseResp.getInput());
        String outputPath = ossFileService.uploadFile(testCaseResp.getExpectOutput());

        // 建立 TestCase 对象
        TestCase testCase = new TestCase(testCaseResp.getId(), testCaseResp.getQuestionId(), inputPath, outputPath);

        // 修改测试用例
        boolean isSuccess = updateById(testCase);

        if (isSuccess) {
            return Resp.success("成功修改测试用例！");
        } else {
            return Resp.fail("修改失败！");
        }
    }

    @Override
    public Resp<String> deleteTestCase(Long id) {

        /*
         * 这里要检查是否具有管理员权限
         *
         */

        // 如果具有管理员权限
        boolean isSuccess = removeById(id);
        if (isSuccess) {
            return Resp.success("成功删除测试用例！");
        } else {
            return Resp.fail("删除失败！");
        }
    }

    @Override
    public Resp<String> publishTestCases(TestCasesResp testCasesResp) {
        List<String> inputList = testCasesResp.getInputList();
        List<String> outputList = testCasesResp.getExpectOutputList();

        int casesNum = inputList.size();

        Integer questionId = testCasesResp.getQuestionId();

        for (int i = 0; i < casesNum; i++) {
            TestCaseResp testCaseResp = new TestCaseResp();
            testCaseResp.setQuestionId(questionId);
            testCaseResp.setInput(inputList.get(i));
            testCaseResp.setExpectOutput(outputList.get(i));
            publishTestCase(testCaseResp);
        }

        return Resp.success("测试用例发布成功！");
    }

    @Override
    public Resp<List<TestCaseVO>> getTestCasesByQuestionId(Integer questionId) throws IOException {
        QueryWrapper<TestCase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("question_id", questionId);
        List<TestCase> testCaseList = testCaseMapper.selectList(queryWrapper);
        List<TestCaseVO> testCaseVOS = convertTestCaseToVO(testCaseList);
        return Resp.success(testCaseVOS);
    }

    /*
     * @Author YFMan
     * @Description 将包含url的oss对象转化为string类型，并返回给前端进行渲染
     * @Date 2023/4/28 16:53
     * @Param [testCaseList]
     * @return java.util.List<com.example.server.vo.TestCaseVO>
     **/
    List<TestCaseVO> convertTestCaseToVO(List<TestCase> testCaseList) throws IOException {
        List<TestCaseVO> testCaseVOS = new ArrayList<>();
        for (TestCase testCase : testCaseList) {
            // 创建 VO 对象
            TestCaseVO testCaseVO = new TestCaseVO();
            // 初始化 id 和 questionId
            testCaseVO.setId(testCase.getId());
            testCaseVO.setQuestionId(testCase.getQuestionId());

            String input = stringRedisTemplate.opsForValue().get(testCase.getInputPath());
            if (input == null) {
                input = ossFileService.downloadFile(testCase.getInputPath());
                stringRedisTemplate.opsForValue().set(testCase.getInputPath(), input);
            }
            testCaseVO.setInput(input);

            String expectOutput = stringRedisTemplate.opsForValue().get(testCase.getOutputPath());
            if (expectOutput == null) {
                expectOutput = ossFileService.downloadFile(testCase.getOutputPath());
                stringRedisTemplate.opsForValue().set(testCase.getOutputPath(), expectOutput);
            }
            testCaseVO.setExpectOutput(expectOutput);
            // 加入到数组中
            testCaseVOS.add(testCaseVO);
        }
        return testCaseVOS;
    }
}
