package com.example.server.dto.Question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDailyQuestionResp {
    private LocalDate date;
    private String prob_title;
    private int prob_id;
    private boolean state;
}
