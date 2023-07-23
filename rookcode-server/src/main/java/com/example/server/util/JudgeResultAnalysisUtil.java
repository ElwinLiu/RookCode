package com.example.server.util;

import com.example.server.enumeration.JudgeResultEnum;
import com.example.server.pojo.JudgeResult;
import com.example.server.pojo.SingleJudgeResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YFMan
 * @Description TODO 分析 Judge-Core 产生的结果，然后返回给前端 Web 进行渲染
 * @Date 2023/4/24 16:41
 */
public class JudgeResultAnalysisUtil {
    private final JudgeResult judgeResult;
    private Integer timeCost;
    private Integer memoryCost;

    private String judgeCondition;

    private Integer accessNum;

    private String extraInfo;

    public JudgeResultAnalysisUtil(JudgeResult judgeResult) {
        this.judgeResult = judgeResult;
        accessNum = 0;
    }

    public void executeAnalysis(){
        countMaxTimeAndMaxMemoryCost();
        judgeCondition = countJudgeResult().getMessage();
        extraInfo = countExtraInfo();
    }

    /*
     * @Author YFMan
     * @Description //TODO 根据评测结果，计算评测耗费的时间、内存（是以所有测试用例中最大的值决定的）
     * @Date 2023/4/24 16:57
     * @Param
     * @return
     **/
    private void countMaxTimeAndMaxMemoryCost() {
        List<SingleJudgeResult> testCaseResultDTOList = judgeResult.getJudgeResults();
        int time = 0;
        int memory = 0;
        for (SingleJudgeResult testCase : testCaseResultDTOList) {
            int maxTime = countMaxTime(testCase);
            int maxMemory = countMaxMemory(testCase);
            if (maxTime > time) {
                time = maxTime;
            }
            if (maxMemory > memory) {
                memory = maxMemory;
            }
        }
        setTimeCost(time);
        setMemoryCost(memory);
    }

    public JudgeResultEnum countJudgeResult() {
        List<SingleJudgeResult> testCaseResultDTOList = judgeResult.getJudgeResults();
        for (SingleJudgeResult resultDTO : testCaseResultDTOList) {
            JudgeResultEnum condition = JudgeResultEnum.toJudgeResultType(resultDTO.getCondition());
            // WA 或者没有condition，我们返回WA
            if (condition == null || condition == JudgeResultEnum.WRONG_ANSWER) {
                return JudgeResultEnum.WRONG_ANSWER;
            }
            // 其他情况，我们返回第一个（不是ac）
            if (condition != JudgeResultEnum.ACCEPTED) {
                return condition;
            }
            accessNum++;
        }
        // 当所有都ac了
        return JudgeResultEnum.ACCEPTED;
    }

    private Integer countMaxMemory(SingleJudgeResult testCase) {
        Integer memoryCost = testCase.getMemoryCost();
        return memoryCost == null ? 0 : memoryCost;
    }

    private Integer countMaxTime(SingleJudgeResult testCase) {
        Integer cpuTimeCost = testCase.getCpuTimeCost();
        if (cpuTimeCost == null) {
            cpuTimeCost = 0;
        }
        return cpuTimeCost;
    }

    /*
     * @Author YFMan
     * @Description //TODO 统计额外信息，因为额外信息包含多行，每一行为字符串中的一项，
     *                 所以将他们拼接起来就是完整的报错信息了
     * @Date 2023/4/24 17:26
     * @Param []
     * @return java.lang.String
     **/
    private String countExtraInfo(){
        List<String> extraInfoList = judgeResult.getExtraInfo();
        StringBuilder res = new StringBuilder();
        for(String extraInfo : extraInfoList){
            res.append(extraInfo).append(" ");
        }
        return res.toString();
    }

    public void setTimeCost(Integer timeCost) {
        this.timeCost = timeCost;
    }

    public void setMemoryCost(Integer memoryCost) {
        this.memoryCost = memoryCost;
    }

    public Integer getTimeCost() {
        return timeCost;
    }

    public Integer getMemoryCost() {
        return memoryCost;
    }

    public Integer getAccessNum() {
        return accessNum;
    }

    public String getJudgeCondition() {
        return judgeCondition;
    }

    public String getExtraInfo() {
        return extraInfo;
    }
}
