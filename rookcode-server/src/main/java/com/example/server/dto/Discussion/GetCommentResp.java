package com.example.server.dto.Discussion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCommentResp {
    int comment_id;
    int user_id;
    String nickname;
    String avatar;
    LocalDateTime datetime;
    String content;
    String account;
}
