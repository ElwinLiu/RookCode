package com.example.server.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class OssFileServiceImplTest {

    @Autowired
    OssFileServiceImpl service;

    @Test
    void uploadFile() {
        String str = "1234567";
        service.uploadFile(str);
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Test
    void redisSetTest(){
        stringRedisTemplate.opsForValue().set("testKey","testValue");
        String res = stringRedisTemplate.opsForValue().get("testKey1");
        System.out.println(res);
    }

    @Test
    void downloadFile() throws IOException {
        String res = service.downloadFile("http://rtb468wxs.hn-bkt.clouddn.com/FpXW-A_qo7yMlv3B6AT1AwKVSqRn");
        System.out.println(res);
    }
}
