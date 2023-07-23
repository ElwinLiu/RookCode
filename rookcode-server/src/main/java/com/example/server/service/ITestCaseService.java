package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.dto.Judgement.TestCaseResp;
import com.example.server.dto.Resp;
import com.example.server.dto.TestCase.TestCasesResp;
import com.example.server.pojo.TestCase;
import com.example.server.vo.TestCaseVO;

import java.io.IOException;
import java.util.List;

/**
 * @author YFMan
 * @Description 定义用于操作 test_case 表的方法，并将它们映射到 TestCaseMapper 中定义的方法。
 * @Date 2023/4/22 15:31
 */
public interface ITestCaseService extends IService<TestCase> {
    Resp<String> publishTestCase(TestCaseResp testCaseResp);

    Resp<String> updateTestCase(TestCaseResp testCaseResp);

    Resp<String> deleteTestCase(Long id);

    Resp<String> publishTestCases(TestCasesResp testCasesResp);

    Resp<List<TestCaseVO>> getTestCasesByQuestionId(Integer questionId) throws IOException;
}
