package com.example.server.controller;

import com.example.server.dto.Resp;
import com.example.server.dto.TestCase.TestCasesResp;
import com.example.server.pojo.TestCase;
import com.example.server.dto.Judgement.TestCaseResp;
import com.example.server.service.ITestCaseService;
import com.example.server.service.IUsersService;
import com.example.server.util.JwtUtils;
import com.example.server.vo.TestCaseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author YFMan
 * @Description
 * @Date 2023/4/22 15:59
 */
@RestController
@Api(tags = "测试用例类控制器")
@RequestMapping("/test-case")
public class TestCaseController {
    @Autowired
    ITestCaseService testCaseService;
    @Autowired
    IUsersService usersService;

    @ApiOperation(value = "发布测试用例", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/PubTestCase")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<String> PubTestCase(@RequestBody TestCaseResp testCaseResp, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        if (!usersService.is_admin(account)) return Resp.fail("不是管理员，权限不足！");
        return testCaseService.publishTestCase(testCaseResp);
    }

    @ApiOperation(value = "根据 id 修改测试用例", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/UpdateTestCase")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<String> UpdateTestCase(@RequestBody TestCaseResp testCaseResp, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        if (!usersService.is_admin(account)) return Resp.fail("不是管理员，权限不足！");
        return testCaseService.updateTestCase(testCaseResp);
    }

    @ApiOperation(value = "根据 id 删除测试用例，注意 id 的类型为 long", authorizations = {@Authorization(value = "Authorization")})
    @DeleteMapping("/DeleteTestCase/{id}")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<String> deleteTestCase(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        if (!usersService.is_admin(account)) return Resp.fail("不是管理员，权限不足！");
        return testCaseService.deleteTestCase(id);
    }

    @ApiOperation(value = "根据题目 id 发布多个测试用例", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/PubTestCases")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<String> pubTestCases(@RequestBody TestCasesResp testCasesResp, HttpServletRequest httpServletRequest){
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        if (!usersService.is_admin(account)) return Resp.fail("不是管理员，权限不足！");
        return testCaseService.publishTestCases(testCasesResp);
    }

    @ApiOperation(value = "根据题目 id 获取该题目的所有测试用例", authorizations = {@Authorization(value = "Authorization")})
    @GetMapping("/GetTestCases/{questionId}")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<List<TestCaseVO>> getTestCasesByQuestionId(@PathVariable Integer questionId, HttpServletRequest httpServletRequest) throws IOException {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        if (!usersService.is_admin(account)) return Resp.fail("不是管理员，权限不足！");
        return testCaseService.getTestCasesByQuestionId(questionId);
    }
}
