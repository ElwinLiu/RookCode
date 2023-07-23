package com.example.server.service.impl;

import com.example.server.dto.Follow.FollowResp;
import com.example.server.dto.Follow.FollowRespV3;
import com.example.server.dto.Resp;
import com.example.server.service.IFollowsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FollowsServiceImplTest {

    @Autowired
    IFollowsService followsService;

    @Test
    void followOthers() {
        Resp<String> resp = followsService.followOthers("elwin@163.com", "elwin_lxy@163.com");
        System.out.println(resp);
    }

    @Test
    void unFollowOthers(){
        Resp<String> resp = followsService.unFollowOthers("elwin@163.com", "elwin_lxy@163.com");
        System.out.println(resp);
    }

    @Test
    void getFolloweeList() {
        Resp<FollowResp> resp = followsService.getFolloweeList("1426887306@qq.com","lzxin@cug.edu.cn",2,1);
        System.out.println(resp);
    }

    @Test
    void countFollowersByAccount() {
        int count = followsService.countFollowersByAccount("elwin@163.com");
        System.out.println(count);
    }

    @Test
    void countFolloweesByAccount() {
        int count = followsService.countFolloweesByAccount("elwin@163.com");
        System.out.println(count);
    }

    @Test
    void getFollowerListV2() {
        Resp<FollowResp> resp = followsService.getFollowerListV2("elwin@163.com", 1, 2);
        System.out.println(resp);
    }

    @Test
    void getFolloweeListV2() {
        Resp<FollowResp> resp = followsService.getFolloweeListV2("lzxin@cug.edu.cn", 1, 2);
        System.out.println(resp);
    }

    @Test
    void getFollowerListV3() {
        Resp<FollowRespV3> resp = followsService.getFollowerListV3("elwin@163.com", "1426887306@qq.com", 1, 20);
        System.out.println(resp);
    }

    @Test
    void getFolloweeListV3() {
        Resp<FollowRespV3> resp = followsService.getFolloweeListV3("elwin@163.com", "1426887306@qq.com", 1, 2);
        System.out.println(resp);
    }
}
