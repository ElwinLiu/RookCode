package com.example.server.pojo;

import java.util.List;

/**
 * @author YFMan
 * @Description 判题机判题结果实体类
 * @Date 2023/4/24 15:34
 */
public class JudgeResult {
    List<SingleJudgeResult> judgeResults;
    private String submissionId;
    private Long judgeEndTime;
    private List<String> extraInfo;

    public JudgeResult(List<SingleJudgeResult> judgeResults, String submissionId, Long judgeEndTime, List<String> extraInfo) {
        this.judgeResults = judgeResults;
        this.submissionId = submissionId;
        this.judgeEndTime = judgeEndTime;
        this.extraInfo = extraInfo;
    }

    // 必须要添加默认构造函数，否则，在进行反序列化的时候会出错
    public JudgeResult() {
    }

    public List<SingleJudgeResult> getJudgeResults() {
        return judgeResults;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public Long getJudgeEndTime() {
        return judgeEndTime;
    }

    public List<String> getExtraInfo() {
        return extraInfo;
    }


}
