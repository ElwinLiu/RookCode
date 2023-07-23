package com.example.server.vo;

import java.time.LocalDateTime;

/**
 * @author YFMan
 * @Description 用户提交记录的详情类
 * @Date 2023/4/26 9:34
 */
public class RecordsDetailVO {

    // 题目id
    private Integer questionId;

    // 题目title
    private String questionTitle;

    // 测试用例总数
    private Integer testCaseTotalNum;

    // 通过测试用例数量
    private Integer testCaseAccessNum;

    // 语言名字
    private String langName;


    // 提交记录
    private String result;


    // 执行用时
    private Integer execTime;


    // 运行内存
    private Float memory;


    // 提交时间
    private LocalDateTime submitDate;

    // 提交代码
    private String submitCode;

    // 报错信息
    private String extraInfo;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public Integer getTestCaseTotalNum() {
        return testCaseTotalNum;
    }

    public void setTestCaseTotalNum(Integer testCaseTotalNum) {
        this.testCaseTotalNum = testCaseTotalNum;
    }

    public Integer getTestCaseAccessNum() {
        return testCaseAccessNum;
    }

    public void setTestCaseAccessNum(Integer testCaseAccessNum) {
        this.testCaseAccessNum = testCaseAccessNum;
    }

    public String getLangName() {
        return langName;
    }

    public void setLangName(String langName) {
        this.langName = langName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getExecTime() {
        return execTime;
    }

    public void setExecTime(Integer execTime) {
        this.execTime = execTime;
    }

    public Float getMemory() {
        return memory;
    }

    public void setMemory(Float memory) {
        this.memory = memory;
    }

    public LocalDateTime getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(LocalDateTime submitDate) {
        this.submitDate = submitDate;
    }

    public String getSubmitCode() {
        return submitCode;
    }

    public void setSubmitCode(String submitCode) {
        this.submitCode = submitCode;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }
}
