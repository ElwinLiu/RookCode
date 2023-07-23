package com.example.server.dto.Record;

/**
 * @author YFMan
 * @Description 前端 传递给 后端 的数据
 * @Date 2023/4/23 18:44
 */
public class SubmitResp {
    // 题目 id
    private Integer questionId;

    // 用户提交代码
    private String submissionCode;

    // 用户选择的编程语言
    private String language;

    public Integer getQuestionId() {
        return questionId;
    }

    public String getSubmissionCode() {
        return submissionCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public void setSubmissionCode(String submissionCode) {
        this.submissionCode = submissionCode;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
