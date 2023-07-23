package com.example.server.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Elwin
 * @since 2023-05-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("discussion_like")
@ApiModel(value="DiscussionLike对象", description="")
@AllArgsConstructor
@NoArgsConstructor
public class DiscussionLike implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("discussion_id")
    private Integer discussionId;

    @TableField("user_id")
    private Integer userId;


}
