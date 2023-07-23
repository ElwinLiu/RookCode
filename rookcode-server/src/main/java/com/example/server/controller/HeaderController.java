package com.example.server.controller;

import com.example.server.dto.User.GetUserByTokenResp;
import com.example.server.dto.Resp;
import com.example.server.service.impl.UsersServiceImpl;
import com.example.server.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "网站Header接口需求")
public class HeaderController {
    @Autowired
    UsersServiceImpl usersService;

    @ApiOperation(value = "用户信息接口（携带token）", authorizations = {@Authorization(value = "Authorization")})
    @GetMapping("/header")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<GetUserByTokenResp> getUserByToken(HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return usersService.getUserByToken(account);
    }
}
