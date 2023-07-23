package com.example.server.dto.Discussion;

import lombok.Data;

@Data
public class UpdateDiscussionRequest {
    private int id;
    private String title;
    private String content;
}
