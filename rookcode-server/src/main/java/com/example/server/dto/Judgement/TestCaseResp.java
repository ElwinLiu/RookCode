package com.example.server.dto.Judgement;


/**
 * @author YFMan
 * @Description 前端传递过来的测试用例类
 * @Date 2023/4/22 16:33
 */
public class TestCaseResp {
    private Long id;

    private Integer questionId;

    private String input;

    private String expectOutput;

    public Long getId() {
        return id;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public String getInput() {
        return input;
    }

    public String getExpectOutput() {
        return expectOutput;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setExpectOutput(String expectOutput) {
        this.expectOutput = expectOutput;
    }

    public TestCaseResp(Integer questionId, String input, String expectOutput) {
        this.questionId = questionId;
        this.input = input;
        this.expectOutput = expectOutput;
    }

    public TestCaseResp(Integer questionId, Long problemId, String input, String expectOutput) {
        this.questionId = questionId;
        this.input = input;
        this.expectOutput = expectOutput;
    }

    public TestCaseResp() {
    }
}
