package com.example.server.dto.Comment;

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
