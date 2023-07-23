package com.example.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Solutions对象", description="")
@AllArgsConstructor
@NoArgsConstructor
public class Solutions implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("question_id")
    private Integer questionId;

    @TableField("author_id")
    private Integer authorId;

    private String title;

    private String content;

    @TableField("like_num")
    private Integer likeNum;

    @TableField("view_num")
    private Integer viewNum;

    @TableField("submit_date")
    private LocalDateTime submitDate;

    // 乐观锁版本号
    @Version
    @TableField("version")
    private Integer version;

    public Solutions(Integer questionId, Integer authorId, String title, String content, Integer likeNum, Integer viewNum, LocalDateTime submitDate) {
        this.questionId = questionId;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.likeNum = likeNum;
        this.viewNum = viewNum;
        this.submitDate = submitDate;
    }
}
