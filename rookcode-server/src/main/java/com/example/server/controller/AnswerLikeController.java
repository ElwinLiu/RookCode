package com.example.server.controller;

import com.example.server.dto.Resp;
import com.example.server.service.IAnswerLikeService;
import com.example.server.util.JwtUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author YFMan
 * @Description 题解点赞表 前端控制器
 * @Date 2023/5/4 22:35
 */
@RestController
@RequestMapping("/answer-like")
public class AnswerLikeController {
    @Autowired
    IAnswerLikeService answerLikeService;

    @ApiOperation(value = "点赞或取消点赞",authorizations = {@Authorization(value = "Authorization")})
    @GetMapping("/LikeOrUnlikeAnswer/{solutionId}")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<String> LikeOrUnlikeAnswer(@PathVariable Integer solutionId, HttpServletRequest httpServletRequest){
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return answerLikeService.likeOrUnlikeAnswer(account, solutionId);
    }

}
