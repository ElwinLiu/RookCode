package com.example.server.service.impl;

import com.example.server.dto.Judgement.TestCaseResp;
import com.example.server.dto.Resp;
import com.example.server.service.ITestCaseService;
import com.example.server.vo.TestCaseVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;


@SpringBootTest
class TestCaseServiceImplTest {
    @Autowired
    ITestCaseService testCaseService;


    @Test
    void publishTestCase() {
        TestCaseResp testCaseResp = new TestCaseResp(51, "2 2", "4");
        testCaseService.publishTestCase(testCaseResp);
    }

    @Test
    void updateTestCase() {
        TestCaseResp testCaseResp = new TestCaseResp(7, 51L, "4 4", "8");
        testCaseService.updateTestCase(testCaseResp);
    }

    @Test
    void deleteTestCase() {
        Long id = 7L;
        testCaseService.deleteTestCase(7L);
    }

    @Test
    void getTestCasesByQuestionId() throws IOException {
        Integer id = 51;
        Resp<List<TestCaseVO>> resp = testCaseService.getTestCasesByQuestionId(51);
        System.out.println(resp);
    }
}
