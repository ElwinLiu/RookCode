package com.example.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.dto.Follow.*;
import com.example.server.dto.Resp;
import com.example.server.mapper.FollowsMapper;
import com.example.server.mapper.UsersMapper;
import com.example.server.pojo.Follows;
import com.example.server.pojo.Users;
import com.example.server.service.IFollowsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YFMan
 * @Description 关注表的服务类实现
 * @Date 2023/5/4 18:07
 */
@Service
public class FollowsServiceImpl extends ServiceImpl<FollowsMapper, Follows> implements IFollowsService {

    @Autowired(required = false)
    UsersMapper usersMapper;

    @Autowired(required = false)
    FollowsMapper followsMapper;

    /*
     * @Author YFMan
     * @Description 关注其他用户
     * @Date 2023/5/4 18:34
     * @Param [userAccount, followerAccount]
     * @return com.example.server.dto.Resp<java.lang.String>
     **/
    @Override
    public Resp<String> followOthers(String userAccount, String followerAccount) {
        // 查询当前用户
        Users followeeUsers = findUserByAccount(userAccount);
        if (followeeUsers == null) return Resp.fail("用户不存在！");

        // 查询被关注用户
        Users followerUsers = findUserByAccount(followerAccount);
        if (followerUsers == null) return Resp.fail("被关注用户不存在！");

        if (followeeUsers.getId().equals(followerUsers.getId())) return Resp.fail("不能关注自己！");

        // 判断是否已经关注
        QueryWrapper<Follows> queryWrapperSelect = new QueryWrapper<>();
        queryWrapperSelect.eq("follower_id", followerUsers.getId());
        queryWrapperSelect.eq("followee_id", followeeUsers.getId());
        Follows followsSelect = getOne(queryWrapperSelect);
        if (followsSelect != null) return Resp.fail("已经关注过了！");
        queryWrapperSelect.clear();

        // 创建 follows 对象, 并向关注表中插入数据
        Follows follows = new Follows(followerUsers.getId(), followeeUsers.getId());
        boolean isSuccess = save(follows);
        if (isSuccess) return Resp.success("关注成功！");
        else return Resp.fail("关注失败！");
    }

    @Override
    public Resp<String> unFollowOthers(String userAccount, String followerAccount) {
        // 查询当前用户
        Users followeeUsers = findUserByAccount(userAccount);
        if (followeeUsers == null) return Resp.fail("用户不存在！");

        // 查询被关注用户
        Users followerUsers = findUserByAccount(followerAccount);
        if (followerUsers == null) return Resp.fail("被关注用户不存在！");

        // 判断是否已经关注
        QueryWrapper<Follows> queryWrapperSelect = new QueryWrapper<>();
        queryWrapperSelect.eq("follower_id", followerUsers.getId());
        queryWrapperSelect.eq("followee_id", followeeUsers.getId());
        Follows followsSelect = getOne(queryWrapperSelect);
        if (followsSelect == null) return Resp.fail("还未关注过！");

        // 删除关注表中的数据
        QueryWrapper<Follows> queryWrapperDelete = new QueryWrapper<>();
        queryWrapperDelete.eq("follower_id", followerUsers.getId());
        queryWrapperDelete.eq("followee_id", followeeUsers.getId());
        boolean isSuccess = remove(queryWrapperDelete);
        if (isSuccess) return Resp.success("取消关注成功！");
        else return Resp.fail("取消关注失败！");
    }

    /*
     * @Author YFMan
     * @Description 获取被关注列表
     * @Date 2023/5/4 20:39
     * @Param [account, page]
     * @return com.example.server.dto.Resp<com.example.server.dto.Follow.FollowResp>
     **/
    @Override
    public Resp<FollowResp> getFolloweeList(String myAccount, String account, Integer pageNum, Integer pageSize) {
        // 获取 token 用户的 id
        Integer token_id = findUserByAccount(myAccount).getId();

        Page<Follows> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Follows> queryWrapper = new QueryWrapper<>();
        Integer follower_id = findUserByAccount(account).getId();
        queryWrapper.eq("follower_id", follower_id);
        Page<Follows> followsPage = page(page, queryWrapper);
        if (followsPage != null) {
            FollowResp followResp = new FollowResp();
            // 获取查询结果的总页数
            followResp.setPageTotalNum(followsPage.getPages());
            // 创建一个被关注列表
            List<FollowEntity> followeeList = new ArrayList<>();
            for (Follows follows : followsPage.getRecords()) {
                // 根据 id 查询用户信息
                Integer followee_id = follows.getFolloweeId();
                Users users = usersMapper.selectById(followee_id);
                FollowEntity followEntity = new FollowEntity();

                // 判断 token_id 是否关注了 followee_id
                QueryWrapper<Follows> queryWrapperSelect = new QueryWrapper<>();
                queryWrapperSelect.eq("follower_id", followee_id);
                queryWrapperSelect.eq("followee_id", token_id);
                Follows followsSelect = getOne(queryWrapperSelect);


                // 如果 token_id 已经关注，followed 为 true
                if(followsSelect != null) {
                    followEntity.setFollowed(true);
                    // 进一步判断是否互相关注
                    QueryWrapper<Follows> queryWrapperSelect2 = new QueryWrapper<>();
                    queryWrapperSelect2.eq("follower_id", token_id);
                    queryWrapperSelect2.eq("followee_id", followee_id);
                    Follows followsSelect2 = getOne(queryWrapperSelect2);
                    followEntity.setMutual(followsSelect2 != null);
                }else{
                    // 如果 token_id 没有关注，followed 为 false
                    followEntity.setFollowed(false);
                    // 肯定不是互相关注
                    followEntity.setMutual(false);
                }
                followEntity.setAccount(users.getAccount());
                followEntity.setAvatar(users.getAvatar());
                followEntity.setNickname(users.getNickname());
                followEntity.setDescription(users.getDescription());
                followeeList.add(followEntity);
            }
            followResp.setFollowList(followeeList);
            return Resp.success(followResp);
        } else {
            return null;
        }
    }


    /*
     * @Author YFMan
     * @Description 获取关注列表
     * @Date 2023/5/4 20:39
     * @Param [account, page]
     * @return com.example.server.dto.Resp<com.example.server.dto.Follow.FollowResp>
     **/
    @Override
    public Resp<FollowResp> getFollowerList(String myAccount, String account, Integer pageNum, Integer pageSize) {
        // 获取该函数运行的时间
        long startTime = System.currentTimeMillis();
        // 获取 token 用户的 id
        Integer token_id = findUserByAccount(myAccount).getId();
        Page<Follows> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Follows> queryWrapper = new QueryWrapper<>();
        Integer followee_id = findUserByAccount(account).getId();
        queryWrapper.eq("followee_id", followee_id);
        Page<Follows> followsPage = page(page, queryWrapper);
        if (followsPage != null) {
            FollowResp followResp = new FollowResp();
            // 获取查询结果的总页数
            followResp.setPageTotalNum(followsPage.getPages());
            // 创建一个关注列表
            List<FollowEntity> followerList = new ArrayList<>();
            for (Follows follows : followsPage.getRecords()) {
                // 获取 我关注的人 的 id
                Integer follower_id = follows.getFollowerId();
                // 根据 id 查询用户信息
                Users users = usersMapper.selectById(follower_id);
                FollowEntity followEntity = new FollowEntity();
                // 判断 token_id 是否关注了 follower_id
                QueryWrapper<Follows> queryWrapperSelect = new QueryWrapper<>();
                queryWrapperSelect.eq("follower_id", follower_id);
                queryWrapperSelect.eq("followee_id", token_id);
                Follows followsSelect = getOne(queryWrapperSelect);
                // 如果不为空，说明 token_id 已经关注了 follower_id
                if(followsSelect != null) {
                    followEntity.setFollowed(true);
                    // 进一步判断是否互相关注
                    QueryWrapper<Follows> queryWrapperSelect2 = new QueryWrapper<>();
                    queryWrapperSelect2.eq("follower_id", token_id);
                    queryWrapperSelect2.eq("followee_id", follower_id);
                    Follows followsSelect2 = getOne(queryWrapperSelect2);
                    followEntity.setMutual(followsSelect2 != null);
                }
                else {
                    // 如果为空，说明 token_id 没有关注了 follower_id
                    followEntity.setFollowed(false);
                    // 肯定不是互相关注
                    followEntity.setMutual(false);
                }
                followEntity.setAccount(users.getAccount());
                followEntity.setAvatar(users.getAvatar());
                followEntity.setNickname(users.getNickname());
                followEntity.setDescription(users.getDescription());
                followerList.add(followEntity);
            }
            followResp.setFollowList(followerList);
            // 获取该函数运行的时间
            long endTime = System.currentTimeMillis();
            System.out.println("getFollowerList 程序运行时间：" + (endTime - startTime) + "ms");
            return Resp.success(followResp);
        } else {
            return null;
        }
    }

    // 获取关注数
    @Override
    public Resp<FollowNumResp> getFolloweeAndFollowerCount(String account) {
        // 获取被关注者数
        QueryWrapper<Follows> queryWrapper = new QueryWrapper<>();
        Integer follower_id = findUserByAccount(account).getId();
        queryWrapper.eq("follower_id", follower_id);
        Integer followeeNum = count(queryWrapper);
        queryWrapper.clear();

        // 获取关注者数
        queryWrapper.eq("followee_id", follower_id);
        Integer followerNum = count(queryWrapper);

        // 创建一个关注数实体类
        FollowNumResp followNumResp = new FollowNumResp();
        followNumResp.setFolloweeNum(followeeNum);
        followNumResp.setFollowerNum(followerNum);
        return Resp.success(followNumResp);
    }

    /*
     * @Author YFMan
     * @Description 获取 我关注了 列表
     * @Date 2023/5/6 11:35
     * @Param [account, pageNum, pageSize]
     * @return com.example.server.dto.Resp<com.example.server.dto.Follow.FollowResp>
     **/
    @Override
    public Resp<FollowResp> getFollowerListV2(String account, Integer pageNum, Integer pageSize) {
        // 获取该函数运行的时间
        // long startTime = System.currentTimeMillis();
        int count = followsMapper.countFollowersByAccount(account);
        List<FollowEntity> followerList = followsMapper.getFollowersByAccount(account, ((long) (pageNum - 1) * pageSize), pageSize);

        FollowResp followResp = new FollowResp();
        // 获取查询结果的总页数
        followResp.setPageTotalNum((long) Math.ceil(count * 1.0 / pageSize));
        followResp.setFollowList(followerList);
        // 获取该函数运行的时间
        // long endTime = System.currentTimeMillis();
        // System.out.println("getFollowerListV2 程序运行时间：" + (endTime - startTime) + "ms");
        return Resp.success(followResp);
    }

    /*
     * @Author YFMan
     * @Description 获取 我被关注了 列表
     * @Date 2023/5/6 11:35
     * @Param [account, pageNum, pageSize]
     * @return com.example.server.dto.Resp<com.example.server.dto.Follow.FollowResp>
     **/
    @Override
    public Resp<FollowResp> getFolloweeListV2(String account, Integer pageNum, Integer pageSize) {
        int count = followsMapper.countFolloweesByAccount(account);
        List<FollowEntity> followeeList = followsMapper.getFolloweesByAccount(account, ((long) (pageNum - 1) * pageSize), pageSize);

        FollowResp followResp = new FollowResp();
        // 获取查询结果的总页数
        followResp.setPageTotalNum((long) Math.ceil(count * 1.0 / pageSize));
        followResp.setFollowList(followeeList);
        return Resp.success(followResp);
    }

    // 获取关注列表V3
    @Override
    public Resp<FollowRespV3> getFollowerListV3(String myAccount, String account, Integer pageNum, Integer pageSize) {
        // 获取该函数运行的时间
        long startTime = System.currentTimeMillis();
        // 获取列表数量，用于分页
        int count = followsMapper.countFollowersByAccount(account);
        // 获取列表
        List<FollowEntityV3> followerList = followsMapper.getFollowersByAccountV3(myAccount, account, ((long) (pageNum - 1) * pageSize), pageSize);

        FollowRespV3 followResp = new FollowRespV3();
        // 获取查询结果的总页数
        followResp.setPageTotalNum((long) Math.ceil(count * 1.0 / pageSize));
        followResp.setFollowList(followerList);
        // 获取该函数运行的时间
        long endTime = System.currentTimeMillis();
        System.out.println("getFollowerListV3 程序运行时间：" + (endTime - startTime) + "ms");
        return Resp.success(followResp);
    }

    // 获取被关注列表V3
    @Override
    public Resp<FollowRespV3> getFolloweeListV3(String myAccount, String account, Integer pageNum, Integer pageSize) {
        // 获取列表数量，用于分页
        int count = followsMapper.countFolloweesByAccount(account);
        // 获取列表
        List<FollowEntityV3> followeeList = followsMapper.getFolloweesByAccountV3(myAccount, account, ((long) (pageNum - 1) * pageSize), pageSize);

        FollowRespV3 followResp = new FollowRespV3();
        // 获取查询结果的总页数
        followResp.setPageTotalNum((long) Math.ceil(count * 1.0 / pageSize));
        followResp.setFollowList(followeeList);
        return Resp.success(followResp);
    }

    @Override
    public int countFollowersByAccount(String account) {
        return followsMapper.countFollowersByAccount(account);
    }

    @Override
    public int countFolloweesByAccount(String account) {
        return followsMapper.countFolloweesByAccount(account);
    }

    /*
     * @Author YFMan
     * @Description 根据 account 查询用户
     * @Date 2023/5/4 20:41
     * @Param [account]
     * @return com.example.server.pojo.Users
     **/
    Users findUserByAccount(String account) {
        // 查询当前用户 id
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        return usersMapper.selectOne(queryWrapper);
    }


}
