package com.example.server.dto.Judgement;

/**
 * @author YFMan
 * @Description 测试用例实体类，这个实体用于远程调用Judge Host，所以不关心id、problemId
 * @Date 2023/4/23 18:12
 */
public class TestCaseDTO {
    // 输入（这里是输入文件的 url）
    String stdIn;

    // 输出（这里是输出文件的 url）
    String expectedStdOut;

    public String getStdIn() {
        return stdIn;
    }

    public void setStdIn(String stdIn) {
        this.stdIn = stdIn;
    }

    public String getExpectedStdOut() {
        return expectedStdOut;
    }

    public void setExpectedStdOut(String expectedStdOut) {
        this.expectedStdOut = expectedStdOut;
    }
}
