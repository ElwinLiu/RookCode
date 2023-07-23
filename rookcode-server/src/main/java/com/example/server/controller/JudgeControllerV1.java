package com.example.server.controller;

import com.example.server.dto.Resp;
import com.example.server.dto.Record.SubmitResp;
import com.example.server.service.IJudgeService;
import com.example.server.util.JwtUtils;
import com.example.server.vo.JudgeResultVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author YFMan
 * @Description 用户提交代码，服务器进行判题的控制器
 * @Date 2023/4/24 17:32
 */
@RestController
@Api(tags = "判题类控制器")
@RequestMapping("/submission")
public class JudgeControllerV1 {
    @Autowired
    IJudgeService judgeService;


    @ApiOperation(value = "用户提交代码进行测试", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/submit_code")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<JudgeResultVO> submitCode(@RequestBody SubmitResp submitResp, HttpServletRequest httpServletRequest) throws JsonProcessingException, UnirestException {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return judgeService.judgeQuestion(submitResp, account);
    }
}
