package com.example.server.controller;


import com.example.server.dto.Manager.UserListResp;
import com.example.server.dto.Resp;
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

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Elwin
 * @since 2023-04-27
 */
@RestController
@Api(tags = "用户管理类接口管理")
@RequestMapping("/userManagement")
public class UserManagementController {
    @Autowired
    private IUsersService usersService;
    @Autowired
    private ISolutionsService solutionsService;
    @Autowired
    private IDiscussionsService discussionsService;

    @ApiOperation(value = "获取所有用户接口（携带token）", authorizations = {@Authorization(value = "Authorization")})
    @GetMapping("/getUserList")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<UserListResp> getUserList(@RequestParam("page") int page, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return usersService.getUserList(page, account);
    }

    @ApiOperation(value = "根据用户 id 删除用户", authorizations = {@Authorization(value = "Authorization")})
    @DeleteMapping("/deleteUser/{id}")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<String> deleteQuestion(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return usersService.deleteUser(id, account);
    }

    @ApiOperation(value = "管理员根据题解 id 删除题解", authorizations = {@Authorization(value = "Authorization")})
    @DeleteMapping("/deleteSolution/{id}")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<String> deleteSolution(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return solutionsService.adminDeleteSolution(id, account);
    }

    @ApiOperation(value = "根据话题的id删除话题", authorizations = {@Authorization(value = "Authorization")})
    @DeleteMapping("deleteDiscussion/{id}")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<String> deleteDiscussion(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return discussionsService.adminDeleteDiscussion(id, account);
    }
}
