package com.example.server.dto.Question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemResp {
    // 题目的id
    private int id;
    // 题目的标题
    private String title;
    // 该题的题解数量
    private int solution_num;
    // 通过率
    private int pass_rate;
    // 题目难度
    private String difficulty;
    // 答题解决状态，用户对该题的解决状态，有未解答unanswered，已通过solved，解答过但未通过trieds共3种状态
    private String state;
}
