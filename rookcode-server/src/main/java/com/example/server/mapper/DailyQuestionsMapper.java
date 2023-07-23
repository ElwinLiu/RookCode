package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.pojo.DailyQuestions;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Elwin
 * @since 2023-04-24
 */
public interface DailyQuestionsMapper extends BaseMapper<DailyQuestions> {
    @Select("SELECT * FROM daily_questions WHERE date = #{date}")
    DailyQuestions findByDate(@Param("date")LocalDate date);
}
