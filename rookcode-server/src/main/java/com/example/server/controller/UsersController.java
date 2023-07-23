package com.example.server.controller;


import com.example.server.dto.*;
import com.example.server.dto.User.*;
import com.example.server.pojo.Users;
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
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
@RestController
@Api(tags = "用户类接口管理")
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private IUsersService usersService;
    @Autowired
    private ISolutionsService solutionsService;
    private static final String HEADER_AUTHORIZATION = "Authorization";

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public Resp<LoginResp> login(@RequestBody LoginRequest loginParams, HttpServletRequest request) {
        return usersService.login(loginParams.getAccount(), loginParams.getPassword(), loginParams.getCode(), request);
    }

    @ApiOperation(value = "获取登录4位验证码")
    @GetMapping(value = "/captcha", produces = "image/jpeg")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        usersService.captcha(request, response);
    }

    @ApiOperation(value = "获取邮箱6位验证码")
    @PostMapping("/registerCode")
    public Resp<String> RegisterCode(@RequestBody EmailRegisterRequest email) {
        return usersService.registerCode(email.getEmail());
    }

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public Resp<String> Register(@RequestBody RegisterRequest registerRequest) {
        return usersService.register(registerRequest.getAccount(), registerRequest.getPassword(), registerRequest.getCode());
    }

    @ApiOperation(value = "根据用户的id获取用户信息")
    @PostMapping("/getUserById")
    public Resp<GetUserResp> getUserById(@RequestBody GetUserRequest userRequest) {
        Users user = usersService.getUsersById(userRequest.getId());
        if (user == null) return Resp.fail("用户不存在！");
        GetUserResp resp = new GetUserResp(user.getId(), user.getAccount(),
                user.getNickname(), user.getAvatar(), user.getIsAdmin());
        return Resp.success(resp);
    }

    @ApiOperation(value = "根据用户id更新用户头像", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/updateAvatarById")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<AvatarResp> updateAvatarById(@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return usersService.uploadAvatar(file, account);
    }

    @ApiOperation(value = "更新用户的昵称", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/changeNickname")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<String> changeNickname(@RequestBody ChangeNameRequest request, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return usersService.changeNickName(request.getNickname(), account);
    }

}
