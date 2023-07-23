package com.example.server.dto.Solution;

import lombok.Data;

@Data
public class GetSolutionsByUserRequest {
    private String account;
    private int page;
}
