package com.example.server.dto.Solution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSolutionResp {
    String title;
    String content;
    String avatar;
    int like_num;
    int view_num;
    // 标签
    String tags;
    // 作者id
    int authorId;
    String nickname;
    LocalDateTime dateTime;
    String account;

    // 是否点赞
    boolean isLike;
}
