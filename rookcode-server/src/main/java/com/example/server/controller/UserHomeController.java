package com.example.server.controller;

import com.example.server.dto.*;
import com.example.server.dto.Discussion.GetDiscussionsResp;
import com.example.server.dto.Record.PassInfoListResp;
import com.example.server.dto.Solution.GetSolutionsByUserResp;
import com.example.server.dto.User.UpdateUserRequest;
import com.example.server.dto.User.UserDetailResp;
import com.example.server.dto.User.YearSubmitResp;
import com.example.server.service.IDiscussionsService;
import com.example.server.service.ISolutionsService;
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

@RestController
@Api(tags = "用户主页接口需求")
public class UserHomeController {
    @Autowired
    IUsersService usersService;
    @Autowired
    ISolutionsService solutionsService;
    @Autowired
    IDiscussionsService discussionsService;

    @ApiOperation(value = "用户详情接口（携带token）", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/user/detail")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<UserDetailResp> getUserDetail(@RequestParam("account") String account, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String real_account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        boolean is_self = real_account.equals(account);
        return usersService.getUserDetail(real_account, account, is_self);
    }

    @ApiOperation(value = "用户近一年提交数量接口（不携带token）")
    @GetMapping("/user/year_submit")
    public Resp<List<YearSubmitResp>> getYearSubmit(@RequestParam("account") String account) {
        return usersService.getYearSubmit(account);
    }

    @ApiOperation(value = "获取用户发布的题解接口（不携带token）")
    @GetMapping("user/solutionList")
    public Resp<GetSolutionsByUserResp> getSolutionsByUser(@RequestParam("page") int page, @RequestParam("account") String account) {
        return solutionsService.getSolutionsByUser(page, account);
    }

    @ApiOperation(value = "用户信息更新接口（携带token）", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/user/update")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<String> updateUserInfo(@RequestBody UpdateUserRequest request, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return usersService.updateUserInfo(request.getNickname(), request.getDescription(), request.getAvatar(), account);
    }

    @ApiOperation(value = "用户最近通过记录列表接口（不携带token）")
    @GetMapping("user/pass/list")
    public Resp<PassInfoListResp> getPassInfo(@RequestParam("page") int page, @RequestParam("account") String account) {
        return usersService.getPassInfo(page, account);
    }

    @ApiOperation(value = "获取用户发布的话题接口（不携带token）")
    @GetMapping("user/discussionList")
    public Resp<GetDiscussionsResp> getDiscussionsByUser(@RequestParam("page") int page, @RequestParam("account") String account) {
        return discussionsService.getDiscussionsByUser(page, account);
    }

}
