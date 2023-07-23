package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.dto.*;
import com.example.server.dto.Manager.UserListResp;
import com.example.server.dto.Record.PassInfoListResp;
import com.example.server.dto.User.*;
import com.example.server.pojo.Users;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
public interface IUsersService extends IService<Users> {
    /**
     * 用户登录
     * @param account
     * @param password
     * @param code
     * @param request
     * @return
     */
    Resp<LoginResp> login(String account, String password, String code, HttpServletRequest request);

    /**
     * 获取登录验证码
     * @param request
     * @param response
     */
    void captcha(HttpServletRequest request, HttpServletResponse response);

    /**
     * 获取邮箱注册验证码
     * @param email
     * @return
     */
    Resp<String> registerCode(String email);

    /**
     * 注册账号
     * @param account
     * @param password
     * @param code
     * @return
     */
    Resp<String> register(String account, String password, String code);

    /**
     * 修改用户个人信息, account用于校验是否为本人操作
     * @param name
     * @param account
     * @return
     */
     Resp<String> changeNickName(String name, String account);

    /**
     * 上传用户头像, account用于校验是否为本人操作
     * @param file
     * @param account
     * @return
     */
    Resp<AvatarResp> uploadAvatar(MultipartFile file, String account);

    /**
     * 根据用户token获取account，传入该函数，查询用户的基本信息
     * @param account
     * @return
     */
    Resp<GetUserByTokenResp> getUserByToken(String account);

    /**
     * 检查用户account和传入的id是否在同一个账号下
     * @param id
     * @param account
     * @return
     */
    int checkAuth(int id, String account);

    /**
     * 获取用户详情
     * @param account 用户的账号
     * @return 用户详情
     */
    Resp<UserDetailResp> getUserDetail(String myAccount,String account, boolean r_is_self);

    /**
     * 获取用户这一年的提交记录
     * @param account 用户的账号
     * @return 返回提交记录
     */
    Resp<List<YearSubmitResp>> getYearSubmit(String account);

    /**
     * 更新用户信息
     * @param nickname 用户的新昵称
     * @param description 用户的个人简介
     * @param avatar 用户的新头像——base64/url传递
     * @return 返回成功/失败消息
     */
    Resp<String> updateUserInfo(String nickname, String description, String avatar, String account);

    /**
     * 获取用户最近通过的记录
     * @param page 页数
     * @param account 用户账号
     * @return 通过记录
     */
    Resp<PassInfoListResp> getPassInfo(int page, String account);

    /**
     * 判断用户是否为管理员
     * @param account 用户的账号
     * @return boolean
     */
    boolean is_admin(String account);

    Resp<UserListResp> getUserList(int page, String account);

    Resp<String> deleteUser(int id, String account);

    /******************************* 操作数据库 *******************************/

    // 根据账号查询用户
    Users getUsersByAccount(String account, String password);

    // 更新用户头像
    int updateAvatarById(int id, String url);

    // 通过用户id更新用户昵称
    int updateNicknameById(int id, String name);

    // 根据账号用户ID查询用户
    Users getUsersById(int id);

    // 注册用户到数据库
    int insertUser(Users user);



}
