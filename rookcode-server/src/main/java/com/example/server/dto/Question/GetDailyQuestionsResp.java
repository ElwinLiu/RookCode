package com.example.server.dto.Question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDailyQuestionsResp {
    private List<GetDailyQuestionResp> questionRespList;
}
