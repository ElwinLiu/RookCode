package com.example.server.dto.Question;

import com.example.server.dto.Question.ProblemResp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowProblemListResp {
    private int total_page;
    private List<ProblemResp> problemList;
}
