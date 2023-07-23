package com.example.server.dto.Record;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassInfoResp {
    // 状态为通过的提交记录的id
    int id;
    // 所提交的题目的标题
    String title;
    // 提交日期
    LocalDateTime date;
    int question_id;
}
