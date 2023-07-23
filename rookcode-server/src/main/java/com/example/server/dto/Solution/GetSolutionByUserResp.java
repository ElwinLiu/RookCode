package com.example.server.dto.Solution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSolutionByUserResp {
    private int id;
    private String question_title;
    private String solution_title;
    private int view;
    private int like;
    private LocalDateTime date;
    private int question_id;
}
