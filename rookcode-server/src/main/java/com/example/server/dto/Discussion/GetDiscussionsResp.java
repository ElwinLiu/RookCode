package com.example.server.dto.Discussion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDiscussionsResp {
    List<GetDiscussionResp> discussionList;
    int total_cnt;
}
