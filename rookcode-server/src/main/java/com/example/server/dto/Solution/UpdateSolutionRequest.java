package com.example.server.dto.Solution;

import lombok.Data;

@Data
public class UpdateSolutionRequest {
    // 题解的id
    private int solution_id;
    // 标题
    private String title;
    // 内容
    private String content;
    // 标签
    private String tags;
}
