package com.example.server.dto.Question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResp {
    // 题目编号
    private int id;
    // 题目标题
    private String title;
    // 题目html内容
    private String content;
    // 题目标签
    private List<String> tags;
    // 题目难度
    private int level;
}
