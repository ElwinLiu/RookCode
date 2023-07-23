package com.example.server.vo;

/**
 * @author YFMan
 * @Description 返回给前端进行渲染的测试用例——包含用例数据，而非url地址
 * @Date 2023/4/28 16:49
 */
public class TestCaseVO {
    private Long id;
    private Integer questionId;
    private String input;
    private String expectOutput;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getExpectOutput() {
        return expectOutput;
    }

    public void setExpectOutput(String expectOutput) {
        this.expectOutput = expectOutput;
    }
}
