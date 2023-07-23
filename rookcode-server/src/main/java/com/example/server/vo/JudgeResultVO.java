package com.example.server.vo;

/**
 * @author YFMan
 * @Description 后端传递给 前端 前端进行渲染的对象 view object
 * @Date 2023/4/23 18:40
 */
public class JudgeResultVO {
    // 通过的测试用例数量
    private Integer accessNum;

    // 测试用例总数量
    private Integer totalNum;

    // 判题结果/代码运行状态
    private String judgeCondition;

    // 额外信息（如果access则为空，否则就是报错信息）
    private String extraInfo;

    // 时间消耗
    private Integer timeCost;

    //  内存消耗
    private Integer memoryCost;

    public Integer getAccessNum() {
        return accessNum;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public String getJudgeCondition() {
        return judgeCondition;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public Integer getTimeCost() {
        return timeCost;
    }

    public Integer getMemoryCost() {
        return memoryCost;
    }

    public void setAccessNum(Integer accessNum) {
        this.accessNum = accessNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public void setJudgeCondition(String judgeCondition) {
        this.judgeCondition = judgeCondition;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public void setTimeCost(Integer timeCost) {
        this.timeCost = timeCost;
    }

    public void setMemoryCost(Integer memoryCost) {
        this.memoryCost = memoryCost;
    }

    @Override
    public String toString() {
        return "JudgeResultVO{" +
                "accessNum=" + accessNum +
                ", totalNum=" + totalNum +
                ", judgeCondition='" + judgeCondition + '\'' +
                ", extraInfo='" + extraInfo + '\'' +
                ", timeCost=" + timeCost +
                ", memoryCost=" + memoryCost +
                '}';
    }
}
