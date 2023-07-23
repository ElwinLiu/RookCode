package com.example.server.controller;


import com.example.server.dto.Discussion.*;
import com.example.server.dto.Resp;
import com.example.server.service.IDiscussionsService;
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
 * @since 2023-05-05
 */
@RestController
@RequestMapping("/discussions")
@Api(tags = "讨论类接口管理")
public class DiscussionsController {
    @Autowired
    private IDiscussionsService discussionsService;
    @ApiOperation(value = "获取讨论列表", authorizations = {@Authorization(value = "Authorization")})
    @GetMapping("/getDiscussions")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    Resp<GetDiscussionsResp> getDiscussions(@RequestParam("page") int page, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return discussionsService.getDiscussions(page, account);
    }

    @ApiOperation(value = "搜索讨论列表", authorizations = {@Authorization(value = "Authorization")})
    @GetMapping("/findDiscussions")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    Resp<GetDiscussionsResp> findDiscussions(@RequestParam("page") int page,@RequestParam(value = "title", required = false)String title, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return discussionsService.findDiscussions(page, account, title);
    }

    @ApiOperation(value = "根据话题的id获取详情", authorizations = {@Authorization(value = "Authorization")})
    @GetMapping("/getDiscussionById")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    Resp<GetDiscussionByIdResp> getDiscussionById(@RequestParam("discussion_id") int discussion_id, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return discussionsService.getDiscussionById(discussion_id, account);
    }

    @ApiOperation(value = "发布讨论", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/pubDiscussion")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    Resp<Integer> pubDiscussion(@RequestBody PubDiscussionRequest request, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return discussionsService.pubDiscussion(request.getTitle(), request.getContent(), account);
    }

    @ApiOperation(value = "评论讨论区的话题", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/commentDiscussion")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    Resp<Integer> commentDiscussion(@RequestBody CommentRequest requestComment, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return discussionsService.commentDiscussion(requestComment.getDiscussion_id(), requestComment.getContent(), account);
    }

    @ApiOperation(value = "获取话题的评论")
    @GetMapping("/getComments")
    Resp<GetCommentsResp> getComments(@RequestParam("page") int page, @RequestParam("discussion_id") int discussion_id) {
        return discussionsService.getComments(page, discussion_id);
    }

    @ApiOperation(value = "给话题点赞", authorizations = {@Authorization(value = "Authorization")})
    @GetMapping("/likeDiscussion")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    Resp<Boolean> likeDiscussion(@RequestParam("discussion_id")int discussion_id, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return discussionsService.like(discussion_id, account);
    }

    @ApiOperation(value = "编辑话题", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("/updateDiscussion")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    Resp<String> updateDiscussion(@RequestBody UpdateDiscussionRequest request, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return discussionsService.updateDiscussion(request.getId(), request.getTitle(), request.getContent(), account);
    }

    @ApiOperation(value = "根据话题的id删除话题", authorizations = {@Authorization(value = "Authorization")})
    @DeleteMapping("deleteDiscussion/{id}")
    @ApiImplicitParam(name = "Authorization", value = "Authorization",required = true, dataType = "String",paramType="header")
    public Resp<String> deleteDiscussion(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        // 从token获取account
        String account = JwtUtils.getUsernameFromRequest(httpServletRequest);
        return discussionsService.DeleteDiscussion(id, account);
    }

}
