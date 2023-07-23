package com.example.server.service;


import com.example.server.dto.Resp;
import com.example.server.dto.Record.SubmitResp;
import com.example.server.vo.JudgeResultVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * @author YFMan
 * @Description 判题服务的 具体业务处理逻辑 接口
 * @Date 2023/4/23 18:17
 */

public interface IJudgeService {
    Resp<JudgeResultVO> judgeQuestion(SubmitResp submitResp, String account) throws JsonProcessingException, UnirestException;
    String getAccessToken() throws UnirestException;
}
