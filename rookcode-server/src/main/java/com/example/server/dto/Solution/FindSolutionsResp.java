package com.example.server.dto.Solution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindSolutionsResp {

    List<FindSolutionResp> findSolutionRespList;
    // 总共有多少条题解
    int total_cnt;

}
