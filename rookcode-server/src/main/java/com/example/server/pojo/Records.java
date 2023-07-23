package com.example.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Records对象", description="")
@AllArgsConstructor
@NoArgsConstructor
public class Records implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("question_id")
    private Integer questionId;

    @TableField("lang_id")
    private Integer langId;

    @ApiModelProperty(value = "Depends on results from judger")
    private String result;

    @ApiModelProperty(value = "unit: ms")
    @TableField("exec_time")
    private Integer execTime;

    @ApiModelProperty(value = "memory consumption")
    private Float memory;

    @ApiModelProperty(value = "when did user submit the answer?")
    @TableField("submit_date")
    private LocalDateTime submitDate;

    @TableField("log")
    private String log;

    @TableField("state")
    private int state;

    @TableField("test_case_total_num")
    private int testCaseTotalNum;


    @TableField("test_case_access_num")
    private int testCaseAccessNum;

    @TableField("submit_code")
    private String submitCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getLangId() {
        return langId;
    }

    public void setLangId(Integer langId) {
        this.langId = langId;
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

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getTestCaseTotalNum() {
        return testCaseTotalNum;
    }

    public void setTestCaseTotalNum(int testCaseTotalNum) {
        this.testCaseTotalNum = testCaseTotalNum;
    }

    public int getTestCaseAccessNum() {
        return testCaseAccessNum;
    }

    public void setTestCaseAccessNum(int testCaseAccessNum) {
        this.testCaseAccessNum = testCaseAccessNum;
    }

    public String getSubmitCode() {
        return submitCode;
    }

    public void setSubmitCode(String submitCode) {
        this.submitCode = submitCode;
    }
}
