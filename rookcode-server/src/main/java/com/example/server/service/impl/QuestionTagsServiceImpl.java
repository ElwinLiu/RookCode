package com.example.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.mapper.QuestionTagsMapper;
import com.example.server.pojo.QuestionTags;
import com.example.server.service.IQuestionTagsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Elwin
 * @since 2023-04-19
 */
@Service
public class QuestionTagsServiceImpl extends ServiceImpl<QuestionTagsMapper, QuestionTags> implements IQuestionTagsService {

}
