package com.example.server.dto.Question;

import java.util.List;

/**
 * @author YFMan
 * @Description 用来更新题目的标签 的 data transport object
 * @Date 2023/4/27 12:31
 */
public class UpdateTagsRequest {
    private Integer questionId;
    private List<String> tags;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
