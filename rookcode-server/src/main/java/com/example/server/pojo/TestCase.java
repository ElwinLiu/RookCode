package com.example.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author YFMan
 * @Description 测试用例实体类
 * @Date 2023/4/22 15:10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("test_case")
@ApiModel(value = "TestCase对象",description = "对应题目的测试用例")
public class TestCase implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "测试用例对应的题目的id")
    @TableField("question_id")
    private Integer questionId;

    @ApiModelProperty(value = "测试用例的输入url")
    @TableField("input_path")
    private String inputPath;

    @ApiModelProperty(value = "测试用例期待输出的url")
    @TableField("output_path")
    private String outputPath;

    public TestCase(Long id, Integer problemId, String inputPath, String outputPath) {
        this.id = id;
        this.questionId = problemId;
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }
}
