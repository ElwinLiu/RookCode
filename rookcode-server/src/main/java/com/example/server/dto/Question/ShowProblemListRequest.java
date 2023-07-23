package com.example.server.dto.Question;

import lombok.Data;

import java.util.List;

@Data
public class ShowProblemListRequest {
    // 有4种类型，分别为"none"/"easy"/ "medium"/ "hard"，默认为none，即为查询所有难度的题目
    private String difficulty;
    // 有4种类型，分别为"none", "unanswered" 未解答, "solved" 已通过, "tried" 尝试过即解答过但未通过， 默认选择none，即查询所有状态的题目
    private String state;
    // (题目标签，最多3个): ["binary_search", "string", ...]
    private List<String> tag;
    // (用户输入): "二分搜索"
    private String input;
    // (当前查询页数): 2
    private int page;

}
