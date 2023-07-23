package com.example.server.dto.Comment;

import lombok.Data;

@Data
public class PubCommentRequest {
    int solution_id;
    String content;
}
