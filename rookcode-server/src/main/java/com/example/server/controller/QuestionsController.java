package com.example.server.controller;


import com.example.server.dto.*;
import com.example.server.dto.Question.*;
import com.example.server.service.IQuestionsService;
import com.example.server.service.IUsersService;
import com.example.server.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
@RestController
@Api(tags = "题目类接口管理")
@RequestMapping("/questions")
public class QuestionsController {
    @Autowired
    private IQuestionsService questionsService;
    @Autowired
    private IUsersService usersService;
    @ApiOperation(value = "获取题目数据(默认每页15条)")
    @PostMapping("/getQuestions")
    public Resp<List<QuestionResp>> getQuestions(@RequestBody GetQuestionsRequest questionsRequest) {
        return questionsService.getQuestions(questionsRequest.getPage());
    }

    @ApiOperation(value = "根据id获取题目数据")
    @PostMapping("/getQuestion")
    public Resp<QuestionResp> getQuestion(@RequestBody GetQuestionRequest questionRequest) {
        return questionsService.getQuestion(questionRequest.getId());
    }

    @ApiOperation(value = "根据id获取题目通过信息")
    @GetMapping("/getQuestionInfo")
    public Resp<QuestionInfoResp> getQuestionInfo(@RequestParam("question_id") int question_id) {
        return questionsService.getQuestionInfo(question_id);
    }

    @ApiOperation(value = "发布题目", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/PublishQuestion")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<Integer> publishQuestion(@RequestBody QuestionTagsDTO questionTagsDTO, HttpServletRequest httpServletRequest){
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        if (!usersService.is_admin(account)) return Resp.fail("不是管理员，权限不足！");
        return  questionsService.publishQuestion(questionTagsDTO);
    }

    @ApiOperation(value = "修改题目", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/UpdateQuestion")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<String> updateQuestion(@RequestBody QuestionTagsDTO questionTagsDTO, HttpServletRequest httpServletRequest){
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        if (!usersService.is_admin(account)) return Resp.fail("不是管理员，权限不足！");
        return questionsService.updateQuestion(questionTagsDTO);
    }

    @ApiOperation(value = "根据题目 id 删除题目", authorizations = {@Authorization(value = "Authorization")})
    @DeleteMapping("/DeleteQuestion/{id}")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<String> deleteQuestion(@PathVariable Integer id, HttpServletRequest httpServletRequest){
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        if (!usersService.is_admin(account)) return Resp.fail("不是管理员，权限不足！");
        return questionsService.deleteQuestion(id);
    }

    @ApiOperation(value = "根据题目 id 修改题目难度", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/UpdateQuestionDifficulty")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<String> updateQuestionDifficulty(@RequestBody UpdateDifficultyDTO updateDifficultyDTO, HttpServletRequest httpServletRequest){
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        if (!usersService.is_admin(account)) return Resp.fail("不是管理员，权限不足！");
        return questionsService.updateQuestionDifficulty(updateDifficultyDTO);
    }

    @ApiOperation(value = "根据题目 id 修改题目标签", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/UpdateQuestionTags")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<String> updateQuestionTags(@RequestBody UpdateTagsRequest updateTagsRequest, HttpServletRequest httpServletRequest){
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        if (!usersService.is_admin(account)) return Resp.fail("不是管理员，权限不足！");
        return questionsService.updateQuestionTags(updateTagsRequest);
    }
}
