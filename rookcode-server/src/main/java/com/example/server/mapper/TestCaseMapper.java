package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.pojo.TestCase;

/**
 * @author YFMan
 * @Description 用于操作 test_case 表中的各种方法
 * @Date 2023/4/22 15:26
 */
public interface TestCaseMapper extends BaseMapper<TestCase> {
    Integer saveTestCaseReturnId(TestCase testCase);
}
