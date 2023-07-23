package com.example.server.dto.Discussion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDiscussionResp {
    private int id;
    private String avatar;
    private String title;
    private String description;
    private int view_num;
    private int like_num;
    private int comments_num;
    private boolean is_liked;
    private LocalDateTime dateTime;
}
