package com.example.server.dto.Question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionInfoResp {
    private int users_num;
    private int submit_num;
    private int pass_num;
}
