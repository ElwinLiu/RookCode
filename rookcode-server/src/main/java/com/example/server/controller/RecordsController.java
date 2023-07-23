package com.example.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.dto.Resp;
import com.example.server.pojo.Records;
import com.example.server.pojo.Users;
import com.example.server.service.IRecordsService;
import com.example.server.util.JwtUtils;
import com.example.server.vo.RecordsDetailVO;
import com.example.server.vo.RecordsVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
@RestController
@RequestMapping("/records")
public class RecordsController {
    @Autowired(required = false)
    IRecordsService recordsService;

    @ApiOperation(value = "根据用户 token 和题目 id 获取提交记录列表", authorizations = {@Authorization(value = "Authorization")})
    @GetMapping("/GetList/{questionId}")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<List<RecordsVO>> getRecordList(@PathVariable Integer questionId, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return recordsService.getRecordsList(account, questionId);
    }

    @ApiOperation(value = "根据 recordsId 返回对应的提交记录详情", authorizations = {@Authorization(value = "Authorization")})
    @GetMapping("/GetRecordsDetail/{recordsId}")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<RecordsDetailVO> getRecordsDetail(@PathVariable Integer recordsId){
        return recordsService.getRecordsDetail(recordsId);
    }
}
