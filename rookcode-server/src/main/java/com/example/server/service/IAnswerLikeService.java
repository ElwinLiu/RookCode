package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.dto.Resp;
import com.example.server.pojo.AnswerLike;

/**
 * @author YFMan
 * @Description 题解点赞表 服务类
 * @Date 2023/5/4 22:19
 */
public interface IAnswerLikeService extends IService<AnswerLike> {
    // 点赞或取消点赞，如果查得到 user_id 和 answer_id 的记录，则删除，否则插入
    Resp<String> likeOrUnlikeAnswer(String account, Integer solutionId);

    // 判读用户是否对该题解点过赞
    boolean isLikeAnswer(String account, Integer solutionId);
}
