package com.example.server.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * @author YFMan
 * @Description 用户提交列表的单项Entity
 * @Date 2023/4/26 8:49
 */
public class RecordsVO {
    private Integer id;


    private String langName;


    private String result;


    private Integer execTime;


    private Float memory;


    private LocalDateTime submitDate;

    public RecordsVO(Integer id, String langName, String result, Integer execTime, Float memory, LocalDateTime submitDate) {
        this.id = id;
        this.langName = langName;
        this.result = result;
        this.execTime = execTime;
        this.memory = memory;
        this.submitDate = submitDate;
    }

    public RecordsVO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
