package com.example.server.dto.Discussion;

import com.example.server.dto.Discussion.GetCommentResp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentsResp {
    int total_page;
    List<GetCommentResp> commentList;
}
