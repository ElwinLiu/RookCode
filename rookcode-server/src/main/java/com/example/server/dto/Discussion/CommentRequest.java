package com.example.server.dto.Discussion;

import lombok.Data;

@Data
public class CommentRequest {
    int discussion_id;
    String content;
}
