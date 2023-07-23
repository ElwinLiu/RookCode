package com.example.server.service.impl;

import com.example.server.pojo.Follows;
import com.example.server.mapper.FollowsMapper;
import com.example.server.service.IFollowsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Elwin
 * @since 2023-05-04
 */
@Service
public class FollowsServiceImpl extends ServiceImpl<FollowsMapper, Follows> implements IFollowsService {

}
