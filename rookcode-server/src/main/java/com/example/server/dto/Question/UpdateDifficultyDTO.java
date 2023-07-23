package com.example.server.dto.Question;

/**
 * @author YFMan
 * @Description 修改题目难度的数据传输对象
 * @Date 2023/4/27 12:15
 */
public class UpdateDifficultyDTO {
    private Integer questionId;
    private Integer difficulty;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }
}
