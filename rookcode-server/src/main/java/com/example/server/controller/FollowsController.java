package com.example.server.controller;

import com.example.server.dto.Follow.*;
import com.example.server.dto.Resp;
import com.example.server.service.IFollowsService;
import com.example.server.service.IUsersService;
import com.example.server.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author YFMan
 * @Description 关注表的控制器
 * @Date 2023/5/4 18:06
 */
@RestController
@Api(tags = "关注表的控制器")
@RequestMapping("/follows")
public class FollowsController {
    @Autowired
    IFollowsService followsService;
    @Autowired
    IUsersService usersService;

    @ApiOperation(value = "关注其他用户",authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/FollowOthers")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<String> FollowOthers(@RequestBody FollowerAccountRequest followerAccountRequest, HttpServletRequest httpServletRequest){
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return followsService.followOthers(account, followerAccountRequest.getAccount());
    }

    @ApiOperation(value = "取消关注其它用户",authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/UnFollowOthers")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<String> UnFollowOthers(@RequestBody FollowerAccountRequest followerAccountRequest, HttpServletRequest httpServletRequest){
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return followsService.unFollowOthers(account, followerAccountRequest.getAccount());
    }

    @ApiOperation(value = "获取关注者列表",authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/GetFolloweeList")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<FollowResp> GetFolloweeList(@RequestBody GetFollowListRequest getFollowListRequest, HttpServletRequest httpServletRequest){
        // 从 token 获取 token 用户的 account
        String myAccount = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return followsService.getFolloweeList(myAccount, getFollowListRequest.getAccount(),
                getFollowListRequest.getPageNum(), getFollowListRequest.getPageSize());
    }

    @ApiOperation(value = "获取被我关注者（我关注别人）列表", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/GetFollowerList")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<FollowResp> GetFollowerList(@RequestBody GetFollowListRequest getFollowListRequest, HttpServletRequest httpServletRequest){
        // 从 token 获取 token 用户的 account
        String myAccount = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return followsService.getFollowerList(myAccount, getFollowListRequest.getAccount(),
                getFollowListRequest.getPageNum(), getFollowListRequest.getPageSize());
    }

    @ApiOperation(value = "获取当前用户的被关注数和关注数", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/GetFolloweeAndFollowerCount")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<FollowNumResp> GetFolloweeAndFollowerCount(@RequestBody FollowerAccountRequest followerAccountRequest){
        return followsService.getFolloweeAndFollowerCount(followerAccountRequest.getAccount());
    }

    @ApiOperation(value = "获取关注者列表（查询优化接口）",authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/GetFolloweeListV2")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<FollowResp> GetFolloweeListV2(@RequestBody GetFollowListRequest getFollowListRequest){
        return followsService.getFolloweeListV2(getFollowListRequest.getAccount(),
                getFollowListRequest.getPageNum(), getFollowListRequest.getPageSize());
    }

    @ApiOperation(value = "获取被我关注者（我关注别人）列表（查询优化接口）",authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/GetFollowerListV2")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<FollowResp> GetFollowerListV2(@RequestBody GetFollowListRequest getFollowListRequest){
        return followsService.getFollowerListV2(getFollowListRequest.getAccount(), getFollowListRequest.getPageNum(), getFollowListRequest.getPageSize());
    }

    @ApiOperation(value = "获取 关注 列表V3", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/GetFollowerListV3")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<FollowRespV3> GetFollowerListV3(@RequestBody GetFollowListRequest getFollowListRequest, HttpServletRequest httpServletRequest){
        // 从 token 获取 token 用户的 account
        String myAccount = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return followsService.getFollowerListV3(myAccount, getFollowListRequest.getAccount(),
                getFollowListRequest.getPageNum(), getFollowListRequest.getPageSize());
    }

    @ApiOperation(value = "获取 被关注 列表V3",authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/GetFolloweeListV3")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<FollowRespV3> GetFolloweeListV3(@RequestBody GetFollowListRequest getFollowListRequest, HttpServletRequest httpServletRequest){
        // 从 token 获取 token 用户的 account
        String myAccount = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return followsService.getFolloweeListV3(myAccount, getFollowListRequest.getAccount(),
                getFollowListRequest.getPageNum(), getFollowListRequest.getPageSize());
    }




}
