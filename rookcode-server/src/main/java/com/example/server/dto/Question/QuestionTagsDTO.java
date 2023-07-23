package com.example.server.dto.Question;

import java.util.List;

/**
 * @author YFMan
 * @Description 前端传递给后端的题目需要包含标签，该 dto 用来表示题目和 Tags 的集合体
 * @Date 2023/4/26 20:18
 */
public class QuestionTagsDTO {
    // 题目id
    private Integer id;

    // 题目标题
    private String title;

    // 题目内容
    private String content;
    // 难度系数
    private Integer difficulty;
    // 用来表示题目的标签对应的编号
    List<String> tags;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
