package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.dto.Record.PassInfoResp;
import com.example.server.dto.Question.QuestionInfoResp;
import com.example.server.pojo.Records;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
public interface RecordsMapper extends BaseMapper<Records> {
    @Select("SELECT DISTINCT question_id FROM records WHERE user_id = #{userId} AND state = 1")
    List<Integer> selectQuestionIdsByUserIdAndState1(@Param("userId") Integer userId);

    @Select("SELECT DISTINCT question_id FROM records r1 WHERE user_id = #{userId} AND state = 0 AND NOT EXISTS (\n" +
            "  SELECT 1 \n" +
            "  FROM records r2 \n" +
            "  WHERE r2.question_id = r1.question_id AND r2.user_id = r1.user_id AND r2.state = 1\n" +
            ")")
    List<Integer> selectQuestionIdsByUserIdAndState0(@Param("userId") Integer userId);

    @Select("SELECT COUNT(DISTINCT question_id) \n" +
            "FROM records \n" +
            "WHERE user_id = #{userId} AND state = 1 AND question_id IN \n" +
            "  (SELECT id FROM questions WHERE difficulty = #{difficulty})\n")
    Integer SolvedQustionCnt(@Param("userId") Integer userId, @Param("difficulty") Integer difficulty);

    @Select("SELECT r.id, r.submit_date as date, q.title, r.question_id " +
            "FROM records r " +
            "JOIN questions q ON r.question_id = q.id " +
            "WHERE r.user_id = #{userId} AND r.state = 1 " +
            "ORDER BY r.submit_date DESC")
    List<PassInfoResp> getPassInfoList(@Param("userId") int userId);

    @Select("SELECT count(*) " +
            "FROM records r " +
            "JOIN questions q ON r.question_id = q.id " +
            "WHERE r.user_id = #{userId} AND r.state = 1 " +
            "ORDER BY r.submit_date DESC")
    int getPassInfoListCnt(@Param("userId") int userId);

    @Select("SELECT COUNT(DISTINCT r.user_id) AS users_num, COUNT(*) AS submit_num, SUM(r.state = 1) AS pass_num\n" +
            "FROM records r\n" +
            "WHERE r.question_id = #{questionId}\n")
    QuestionInfoResp getQuestionInfo(@Param("questionId") int questionId);
}
