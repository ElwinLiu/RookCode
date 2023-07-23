package com.example.server.pojo;

import com.example.server.enumeration.JudgeResultEnum;
import io.swagger.models.auth.In;

/**
 * @author YFMan
 * @Description 每一个测试用例的判题结果
 * @Date 2023/4/24 15:36
 */
public class SingleJudgeResult {
    private Integer realTimeCost;
    private Integer memoryCost;
    private Integer cpuTimeCost;
    private Integer condition;
    private String stdInPath;
    private String stdOutPath;
    private String stdErrPath;
    private String message;

    /*
     * @Author YFMan
     * @Description 根据 Judge-Core 返回的 condition，设置 message
     * @Date 2023/4/24 16:01
     * @Param []
     * @return void
     **/
    public void setMessageWithCondition() {
        JudgeResultEnum type = JudgeResultEnum.toJudgeResultType(this.condition);
        this.message = type.getMessage();
    }

    public Integer getRealTimeCost() {
        return realTimeCost;
    }

    public Integer getMemoryCost() {
        return memoryCost;
    }

    public Integer getCpuTimeCost() {
        return cpuTimeCost;
    }

    public Integer getCondition() {
        return condition;
    }

    public String getStdInPath() {
        return stdInPath;
    }

    public String getStdOutPath() {
        return stdOutPath;
    }

    public String getStdErrPath() {
        return stdErrPath;
    }

    public String getMessage() {
        return message;
    }

    public void setRealTimeCost(Integer realTimeCost) {
        this.realTimeCost = realTimeCost;
    }

    public void setMemoryCost(Integer memoryCost) {
        this.memoryCost = memoryCost;
    }

    public void setCpuTimeCost(Integer cpuTimeCost) {
        this.cpuTimeCost = cpuTimeCost;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public void setStdInPath(String stdInPath) {
        this.stdInPath = stdInPath;
    }

    public void setStdOutPath(String stdOutPath) {
        this.stdOutPath = stdOutPath;
    }

    public void setStdErrPath(String stdErrPath) {
        this.stdErrPath = stdErrPath;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
