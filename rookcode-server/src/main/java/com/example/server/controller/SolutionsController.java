package com.example.server.controller;


import com.example.server.dto.*;
import com.example.server.dto.Comment.GetCommentsResp;
import com.example.server.dto.Comment.PubCommentRequest;
import com.example.server.dto.Solution.*;
import com.example.server.service.ISolutionsService;
import com.example.server.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
@RestController
@RequestMapping("/solutions")
@Api(tags = "题解类接口管理")
public class SolutionsController {
    @Autowired
    ISolutionsService solutionsService;
    @ApiOperation(value = "发布题解（content字段先URL编码)", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/pubSolution")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<Integer> PubSolution(@RequestBody PubSolutionRequest solutionRequest, HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        String html = solutionRequest.getContent();
        html = URLDecoder.decode(html, StandardCharsets.UTF_8.toString());
        return solutionsService.publishSolution(solutionRequest.getQuestionId(),
                solutionRequest.getTitle(), html, solutionRequest.getTags(), account);
    }

    @ApiOperation(value = "搜索题解", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/findSolutions")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<FindSolutionsResp> findSolution(@RequestBody FindSolutionsRequest request, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return solutionsService.findSolutions(request.getPage(), request.getName(), request.getTags(), request.getQuestion_id(),account);
    }

    @ApiOperation(value = "通过题解id搜索题解", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/getSolutionsById")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<GetSolutionResp> findSolution(@RequestBody GetSolutionRequest request, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return solutionsService.getSolution(account, request.getId());
    }

    @ApiOperation(value = "根据题解的id删除题解", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("deleteSolution")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<String> deleteSolution(@RequestBody GetSolutionRequest request, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return solutionsService.deleteSolution(request.getId(), account);
    }

    @ApiOperation(value = "根据id更新题解", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("updateSolution")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<UpdateSolutionResp> updateSolution(@RequestBody UpdateSolutionRequest request, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return solutionsService.updateSolution(account, request.getSolution_id(), request.getTitle(), request.getContent(), request.getTags());
    }

    @ApiOperation(value = "获取所有标签内容")
    @GetMapping("getAllTags")
    public Resp<String> getAllTags() {
        return solutionsService.getAllTags();
    }

    @ApiOperation(value = "发布评论", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("PublishComment")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<String> publishComment(@RequestBody PubCommentRequest request, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return solutionsService.publishComment(account, request.getSolution_id(), request.getContent());
    }

    @ApiOperation(value = "获取题解的评论（每页10条）")
    @GetMapping("Getcomments")
    public Resp<GetCommentsResp> getComments(@RequestParam("page") int page, @RequestParam("solution_id") int solution_id) {
        return solutionsService.getComments(page, solution_id);
    }
}
