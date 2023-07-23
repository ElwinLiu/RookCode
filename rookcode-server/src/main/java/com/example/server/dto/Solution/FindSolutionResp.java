package com.example.server.dto.Solution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindSolutionResp {
    private int id;
    String title;
    String description;
    String avatar;
    int like_num;
    int view_num;
    // 标签
    String tags;
    // 作者id
    int authorId;
    // 总共有多少条评论
    int comments_cnt;

    // 是否点赞
    boolean isLike;
}
