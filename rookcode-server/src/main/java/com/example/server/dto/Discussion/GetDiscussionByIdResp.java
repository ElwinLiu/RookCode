package com.example.server.dto.Discussion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDiscussionByIdResp {
    private String avatar;
    private String title;
    private String content;
    private int view_num;
    private int like_num;
    private int comments_num;
    private boolean is_liked;
    private LocalDateTime dateTime;
    private int authorId;
    private String nickname;
    private String account;
}
