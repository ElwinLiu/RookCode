package com.example.server.dto.TestCase;

import java.util.List;

/**
 * @author YFMan
 * @Description 用来记录发布多个测试用例的 dto
 * @Date 2023/4/27 20:22
 */
public class TestCasesResp {

    // 题目id
    private Integer questionId;

    // 测试用例输入数组
    private List<String> inputList;

    // 测试用例输出数组
    private List<String> expectOutputList;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public List<String> getInputList() {
        return inputList;
    }

    public void setInputList(List<String> inputList) {
        this.inputList = inputList;
    }

    public List<String> getExpectOutputList() {
        return expectOutputList;
    }

    public void setExpectOutputList(List<String> expectOutputList) {
        this.expectOutputList = expectOutputList;
    }
}
