package com.example.server.dto.Solution;

import lombok.Data;

@Data
public class FindSolutionsRequest {
    int page;
    String name;
    String tags;
    int question_id;
}
