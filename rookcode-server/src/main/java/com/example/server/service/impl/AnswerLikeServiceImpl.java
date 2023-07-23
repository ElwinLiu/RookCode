package com.example.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.dto.Resp;
import com.example.server.mapper.AnswerLikeMapper;
import com.example.server.mapper.SolutionsMapper;
import com.example.server.mapper.UsersMapper;
import com.example.server.pojo.AnswerLike;
import com.example.server.pojo.Solutions;
import com.example.server.pojo.Users;
import com.example.server.service.IAnswerLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author YFMan
 * @Description 题解点赞表 服务实现类
 * @Date 2023/5/4 22:23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AnswerLikeServiceImpl extends ServiceImpl<AnswerLikeMapper, AnswerLike> implements IAnswerLikeService {
    @Autowired(required = false)
    UsersMapper usersMapper;

    @Autowired(required = false)
    SolutionsMapper solutionsMapper;

    /*
     * @Author YFMan
     * @Description 点赞或取消点赞
     * @Date 2023/5/4 22:25
     * @Param [account, answerId]
     * @return com.example.server.dto.Resp<java.lang.String>
     **/
    @Override
    @Transactional
    public Resp<String> likeOrUnlikeAnswer(String account, Integer solutionId) {
        // 根据 account 查找用户 id
        Users users = findUserIdByAccount(account);
        if (users == null) return Resp.fail("用户不存在！");

        Solutions solutions = solutionsMapper.selectById(solutionId);
        if (solutions == null) return Resp.fail("题解不存在！");

        // 判断是否已经点赞
        QueryWrapper<AnswerLike> queryWrapperSelect = new QueryWrapper<>();
        queryWrapperSelect.eq("user_id", users.getId());
        queryWrapperSelect.eq("solution_id", solutionId);
        AnswerLike answerLikeSelect = getOne(queryWrapperSelect);
        // 加写锁
        QueryWrapper<Solutions> wrapper = new QueryWrapper<>();
        // 更新题解点赞数时，加上 for update，防止并发问题
        // wrapper.lambda().eq(Solutions::getId, solutionId).last("for update");
        wrapper.lambda().eq(Solutions::getId, solutionId);
        solutions = solutionsMapper.selectOne(wrapper);
        if (answerLikeSelect != null) {
            // 已经点赞过了，取消点赞
            boolean isSuccess = removeById(answerLikeSelect.getId());
            if (!isSuccess) {
                return Resp.fail("取消点赞失败！");
            }

            // 题解点赞数减一
            // solutions.setLikeNum(solutions.getLikeNum() - 1);
            // solutionsMapper.updateById(solutions);
            // 乐观锁点赞数减一
            isSuccess = decreaseLikeNum(solutionId, solutions.getVersion(), 0);

            // 乐观锁是否执行成功
            if (!isSuccess) {
                return Resp.fail("取消点赞失败！");
            }
            return Resp.success("取消点赞成功！");


        } else {
            // 创建 answerLike 对象, 并向题解点赞表中插入数据
            AnswerLike answerLike = new AnswerLike(users.getId(), solutionId);
            boolean isSuccess = save(answerLike);
            if (!isSuccess) {
                return Resp.fail("点赞失败！");
            }

            // 题解点赞数加一
            //solutions.setLikeNum(solutions.getLikeNum() + 1);
            //solutionsMapper.updateById(solutions);
            // 乐观锁点赞数加一
            isSuccess = increaseLinkNum(solutionId, solutions.getVersion(), 0);

            // 乐观锁点赞失败
            if (!isSuccess) {
                return Resp.fail("点赞失败！");
            }
            return Resp.success("点赞成功！");
        }
    }

    /*
     * @Author YFMan
     * @Description 判读用户是否对该题解点过赞
     * @Date 2023/5/4 23:45
     * @Param [account, solutionId]
     * @return boolean
     **/
    @Override
    public boolean isLikeAnswer(String account, Integer solutionId) {
        // 根据 account 查找用户 id
        Users users = findUserIdByAccount(account);
        if (users == null) return false;

        QueryWrapper<AnswerLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", users.getId());
        queryWrapper.eq("solution_id", solutionId);
        AnswerLike answerLike = getOne(queryWrapper);
        return answerLike != null;
    }

    // 根据 account 查找用户 id
    private Users findUserIdByAccount(String account) {
        // 查询当前用户 id
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        return usersMapper.selectOne(queryWrapper);
    }

    private boolean decreaseLikeNum(Integer solutionId, Integer version, Integer tryTimes) {
        // 乐观锁重试, 重试三次
        if (tryTimes > 3) {
            throw new RuntimeException("乐观锁重试失败！");
        }
        try {
            int rows = solutionsMapper.decreaseLikeNum(solutionId, version);
            if (rows == 0) decreaseLikeNum(solutionId, version, tryTimes + 1);
            return true;
        } catch (OptimisticLockingFailureException e) {
            // 加乐观锁
            QueryWrapper<Solutions> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(Solutions::getId, solutionId);
            Solutions solutions = solutionsMapper.selectOne(wrapper);
            solutionId = solutions.getId();
            version = solutions.getVersion();
            return decreaseLikeNum(solutionId, version, tryTimes + 1);
        }
    }

    // 增加点赞数量
    private boolean increaseLinkNum(Integer solutionId, Integer version, Integer tryTimes) {
        if(tryTimes > 3) throw new RuntimeException("乐观锁重试失败！");
        try {
            int rows = solutionsMapper.increaseLikeNum(solutionId, version);
            if (rows == 0) increaseLinkNum(solutionId, version, tryTimes + 1);
            return rows > 0;
        } catch (OptimisticLockingFailureException e) {
            // 乐观锁重试, 重试三次
            // 加乐观锁
            QueryWrapper<Solutions> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(Solutions::getId, solutionId);
            Solutions solutions = solutionsMapper.selectOne(wrapper);
            solutionId = solutions.getId();
            version = solutions.getVersion();
            return increaseLinkNum(solutionId, version, tryTimes + 1);
        }
    }
}
