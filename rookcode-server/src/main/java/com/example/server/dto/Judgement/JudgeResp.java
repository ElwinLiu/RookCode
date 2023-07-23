package com.example.server.dto.Judgement;

import java.util.List;

/**
 * @author YFMan
 * @Description 判题远程调用的数据传输对象类
 * @Date 2023/4/23 18:03
 */
public class JudgeResp {
    // 用户提交的代码
    private String submissionCode;

    // 限制程序运行的最大时间，如果超过这个时间，强制退出
    // 防止死循环等恶意程序不退出，占用服务器
    private Integer realTimeLimit;

    // 这个是用于判断是否超时的时间限制
    private Integer cpuTimeLimit;

    private Integer memoryLimit;

    // 对程序的输出进行限制，它不能无限制的进行输出
    private Integer outputLimit;

    // 语言（四种取值）
    private String language;

    private String judgePreference;

    // 这道题目对应的测试用例
    private List<TestCaseDTO> solutions;

    public String getSubmissionCode() {
        return submissionCode;
    }

    public Integer getRealTimeLimit() {
        return realTimeLimit;
    }

    public Integer getCpuTimeLimit() {
        return cpuTimeLimit;
    }

    public Integer getMemoryLimit() {
        return memoryLimit;
    }

    public Integer getOutputLimit() {
        return outputLimit;
    }

    public String getLanguage() {
        return language;
    }

    public List<TestCaseDTO> getSolutions() {
        return solutions;
    }

    public void setSubmissionCode(String submissionCode) {
        this.submissionCode = submissionCode;
    }

    public void setRealTimeLimit(Integer realTimeLimit) {
        this.realTimeLimit = realTimeLimit;
    }

    public void setCpuTimeLimit(Integer cpuTimeLimit) {
        this.cpuTimeLimit = cpuTimeLimit;
    }

    public void setMemoryLimit(Integer memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public void setOutputLimit(Integer outputLimit) {
        this.outputLimit = outputLimit;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setSolutions(List<TestCaseDTO> solutions) {
        this.solutions = solutions;
    }

    public String getJudgePreference() {
        return judgePreference;
    }

    public void setJudgePreference(String judgePreference) {
        this.judgePreference = judgePreference;
    }

    @Override
    public String toString() {
        return "JudgeResp{" +
                "submissionCode='" + submissionCode + '\'' +
                ", realTimeLimit=" + realTimeLimit +
                ", cpuTimeLimit=" + cpuTimeLimit +
                ", memoryLimit=" + memoryLimit +
                ", outputLimit=" + outputLimit +
                ", language='" + language + '\'' +
                ", judgePreference='" + judgePreference + '\'' +
                ", solutions=" + solutions +
                '}';
    }
}
