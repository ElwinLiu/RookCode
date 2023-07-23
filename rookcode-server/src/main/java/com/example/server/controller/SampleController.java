package com.example.server.controller;

import com.example.server.dto.Resp;
import com.example.server.dto.User.AvatarResp;
import com.example.server.pojo.DailyQuestions;
import com.example.server.pojo.Users;
import com.example.server.service.IUsersService;
import com.example.server.service.impl.DailyQuestionsServiceImpl;
import com.example.server.util.DailyQuestionScheduler;
import com.example.server.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
@RestController
@Api(tags = "测试类接口管理")
@RequestMapping("/test")
public class SampleController {
    @Autowired
    DailyQuestionsServiceImpl dailyQuestionsService;
    @Autowired
    IUsersService usersService;

    @ApiOperation(value = "jwt sample", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/hello")
    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, dataType = "String", paramType = "header")
    public Resp<Users> getPos() {
        Users user = new Users();
        user.setAccount("ttttt");
        user.setId(1);
        user.setAvatar("sadaw");
        user.setNickname("elwin");
        return Resp.success(user);
    }

    @ApiOperation(value = "hello")
    @PostMapping("/hello5")
    public String hhh() {
        return "呃呃啊啊啊呃";
    }

    @ApiOperation(value = "随机插入一条每日一题的数据")
    @GetMapping("/insertDaily")
    public DailyQuestions inserDaily(@RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return dailyQuestionsService.generateDailyQuestion(date);
    }

    @ApiOperation(value = "根据用户account更新用户头像")
    @PostMapping("/updateAvatarById")
    public Resp<AvatarResp> updateAvatarById(@RequestParam("file") MultipartFile file, @RequestParam("account") String account) {
        // 从token获取account
        return usersService.uploadAvatar(file, account);
    }
}
