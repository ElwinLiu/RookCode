package com.example.server.service.impl;

import com.example.server.dto.Resp;
import com.example.server.mapper.SolutionsMapper;
import com.example.server.service.IAnswerLikeService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class AnswerLikeServiceImplTest {

    @Autowired
    IAnswerLikeService answerLikeService;

    @Autowired(required = false)
    SolutionsMapper solutionsMapper;

    // 测试点赞接口
    @Test
    void likeOrUnlikeAnswer() {
        // 根据 solutionid 查找点赞数量
        Integer likeNum = solutionsMapper.selectById(1).getLikeNum();
        Resp<String> resp = answerLikeService.likeOrUnlikeAnswer("2022959499@qq.com",1);
        // 根据 solutionid 查找点赞数量
        Integer likeNum2 = solutionsMapper.selectById(1).getLikeNum();
        // 判断点赞数量是否加一
        assertEquals(likeNum+1,likeNum2);
        System.out.println(resp);
    }

    // 测试 判断 是否点赞接口
    @Test
    void isLikeAnswer() {
        // 给1号题解点赞
        answerLikeService.likeOrUnlikeAnswer("2022959499@qq.com",1);
        // 调用接口
        boolean b = answerLikeService.isLikeAnswer("2022959499@qq.com", 1);
        // 判断是否点赞
        assertTrue(b);
        // 给 1 号题解取消点赞
        answerLikeService.likeOrUnlikeAnswer("2022959499@qq.com",1);
        // 调用接口
        boolean b2 = answerLikeService.isLikeAnswer("2022959499@qq.com",1);
        assertFalse(b2);
        System.out.println(b);
    }
}
