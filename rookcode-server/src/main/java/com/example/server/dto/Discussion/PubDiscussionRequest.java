package com.example.server.dto.Discussion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PubDiscussionRequest {
    private String title;
    private String content;
}
