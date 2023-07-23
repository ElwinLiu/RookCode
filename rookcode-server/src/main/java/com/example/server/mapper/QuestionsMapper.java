package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.pojo.Questions;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
public interface QuestionsMapper extends BaseMapper<Questions> {

    Integer saveQuestionReturnId(Questions questions);
}
