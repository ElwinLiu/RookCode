package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.server.pojo.Solutions;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
public interface SolutionsMapper extends BaseMapper<Solutions> {
    // 按条件查询题解（指定页数）
    List<Solutions> findSolutions(Page page, @Param("tagName") String tagName,
                                  @Param("titleKeyword") String titleKeyword, @Param("question_id") int question_id);

    // 按条件查询所有题解
    Integer findSolutionsCnt( @Param("tagName") String tagName,
                                      @Param("titleKeyword") String titleKeyword, @Param("question_id") int question_id);

    // 根据 ID 自增 view_num 字段
    void increaseViewNumById(@Param("id") int solution_id);

    // 根据题解的id获取题解的标签
    List<String> findTagsBySolutionId(@Param("solutionId") int solutionId);

    // 增加点赞数量（加乐观锁）
    @Update("update solutions set like_num = like_num + 1, version = version + 1 where id = #{solutionId} and version = #{version}")
    int increaseLikeNum(@Param("solutionId") int solutionId, @Param("version") int version);

    // 减少点赞数量（加乐观锁）
    @Update("update solutions set like_num = like_num - 1, version = version + 1 where id = #{solutionId} and version = #{version}")
    int decreaseLikeNum(@Param("solutionId") int solutionId, @Param("version") int version);
}
