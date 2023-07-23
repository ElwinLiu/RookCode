package com.example.server.service.impl;

import com.example.server.dto.Resp;
import com.example.server.pojo.Records;
import com.example.server.service.IRecordsService;
import com.example.server.vo.RecordsDetailVO;
import com.example.server.vo.RecordsVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;


@SpringBootTest
class RecordsServiceImplTest {

    @Autowired
    IRecordsService recordsService;

    @Test
    void getRecordsList() {
        String account = "1426887306@qq.com";
        Integer QuestionId = 51;
        Resp<List<RecordsVO>> list = recordsService.getRecordsList(account, QuestionId);
/*        System.out.println(list);
        List<RecordsVO> recordsVOList = IRecordsService.convertRecordsToRecordsVO(list.getData());*/
        System.out.println(list);
    }

    @Test
    void getRecordsDetail() {
        Resp<RecordsDetailVO> detailVOResp = recordsService.getRecordsDetail(60);
        System.out.println(detailVOResp);
    }
}
