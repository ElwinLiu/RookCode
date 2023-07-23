package com.example.server.controller;

import com.example.server.dto.*;
import com.example.server.dto.Question.GetDailyQuestionsResp;
import com.example.server.dto.Question.ShowProblemListRequest;
import com.example.server.dto.Question.ShowProblemListResp;
import com.example.server.dto.User.ProgressResp;
import com.example.server.service.IDailyQuestionsService;
import com.example.server.service.IQuestionsService;
import com.example.server.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@RestController
@Api(tags = "首页接口需求")
public class HomeInfoController {
    @Autowired
    IQuestionsService questionsService;
    @Autowired
    IDailyQuestionsService dailyQuestionsService;
    @ApiOperation(value = "问题列表接口（携带token）", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/problem/list")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<ShowProblemListResp> showProblemList(@RequestBody ShowProblemListRequest request, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return questionsService.showProblemList(account, request.getDifficulty(), request.getState(), request.getTag(), request.getInput(), request.getPage());
    }

    @ApiOperation(value = "每日一题接口（携带token）", authorizations = {@Authorization(value = "Authorization")})
    @GetMapping("/daily")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<GetDailyQuestionsResp> getDailyQuestions(@RequestParam("start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start_date, @RequestParam("end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_date, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return dailyQuestionsService.getDailyQuestions(start_date, end_date, account);
    }

    @ApiOperation(value = "用户进度接口（不携带token）")
    @GetMapping("/user/progress")
    public Resp<ProgressResp> getProgress(@RequestParam("account") String account) {
        return questionsService.getProgress(account);
    }

}
