package com.example.server.dto.Solution;

import lombok.Data;

@Data
public class PubSolutionRequest {
    // 问题的id
    private int questionId;
    // 标题
    private String title;
    // 内容
    private String content;
    // 标签
    private String tags;
}
