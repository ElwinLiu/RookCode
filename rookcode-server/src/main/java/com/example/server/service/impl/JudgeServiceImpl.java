package com.example.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.dto.Judgement.JudgeResp;
import com.example.server.dto.Resp;
import com.example.server.dto.Record.SubmitResp;
import com.example.server.dto.Judgement.TestCaseDTO;
import com.example.server.mapper.RecordsMapper;
import com.example.server.mapper.TestCaseMapper;
import com.example.server.mapper.UsersMapper;
import com.example.server.pojo.*;
import com.example.server.service.IJudgeService;
import com.example.server.util.JudgeResultAnalysisUtil;
import com.example.server.vo.JudgeResultVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YFMan
 * @Description 判题服务的 具体业务处理逻辑 实现
 * @Date 2023/4/23 18:20
 */
@Service
public class JudgeServiceImpl implements IJudgeService {

    @Autowired(required = false)
    TestCaseMapper testCaseMapper;

    @Autowired(required = false)
    UsersMapper usersMapper;
    @Autowired(required = false)
    RecordsMapper recordsMapper;

    // 10000ms 10s 程序运行超过这个时间自动杀掉进程
    private int realTimeLimit = 10000;
    // 程序在 cpu 上运行时间 1000ms
    private int cpuTimeLimit = 1000;
    // 程序可以使用的内存限制 单位KB 65536KB = 64 MB
    private int memoryLimit = 65536;
    // 程序输出大小，单位KB 限制为 65536KB = 64MB
    private int outputLimit = 1024;

    private Integer totalSolutionsNum;

    // 远程调用相关配置
    public static final String baseURL = "http://59.****.****.****:8080";
    public static final String JUDGE_URL = "/judge/result";
    public static final String TOKEN_URL = "/auth/access_token";
    public static final String ACCESS_TOKEN_KEY = "accessToken";

    public static final String TEST_ACCESS_TOKEN = "****.****.****";

    // 获取 token 相关
    @Value("${judgehost.token.userId}")
    String userId;
    @Value("${judgehost.token.userSecret}")
    String userSecret;

    /*
     * @Author YFMan
     * @Description 进行判题，分为四步来走。1. 查询数据 2. 调用判题机 3. 分析判题结果
     *                 4. 返回 view object 给前端进行渲染
     * @Date 2023/4/24 9:56
     * @Param [submitResp]
     * @return com.example.server.dto.Resp<com.example.server.vo.JudgeResultVO>
     **/
    @Override
    public Resp<JudgeResultVO> judgeQuestion(SubmitResp submitResp, String account) throws UnirestException, JsonProcessingException {
        // 获取用户的id
        Users user = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account));
        if (user == null) return Resp.fail("该用户不存在");
        int user_id = user.getId();

        // 获取题目的 id
        Integer questionId = submitResp.getQuestionId();

        QueryWrapper<TestCase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("question_id", questionId);
        // 根据 id 查询这道题目的测试用例
        List<TestCase> testCases = testCaseMapper.selectList(queryWrapper);
        if (testCases.size() == 0) {
            return Resp.fail("测试用例不存在，敬请期待！");
        }
        List<TestCaseDTO> testCaseDTOS = convertTestCaseToDTO(testCases);

        // 设置 totalSolutionsNum
        totalSolutionsNum = testCaseDTOS.size();

        JudgeResp judgeResp = new JudgeResp();
        judgeResp.setSolutions(testCaseDTOS);
        judgeResp.setLanguage(submitResp.getLanguage());
        judgeResp.setSubmissionCode(submitResp.getSubmissionCode());

        // 默认设置
        judgeResp.setCpuTimeLimit(cpuTimeLimit);
        judgeResp.setOutputLimit(outputLimit);
        judgeResp.setRealTimeLimit(realTimeLimit);
        judgeResp.setMemoryLimit(memoryLimit);

        // 2. 查询数据完毕，调用判题机
        String res = runJudge(judgeResp);
        ObjectMapper objectMapper = new ObjectMapper();
        JudgeResult judgeResult = null;
        try {
            judgeResult = objectMapper.readValue(res, JudgeResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // 3. 分析判题结果
        JudgeResultVO judgeResultVO = analysisJudgeResult(judgeResult);

        // 4. 插入提交记录
        int r_user_id = user_id;
        int r_question_id = questionId;
        int r_lang = 0;
        String r_result = judgeResultVO.getJudgeCondition();
        int r_execTime = judgeResultVO.getTimeCost();
        float r_memory = judgeResultVO.getMemoryCost();
        LocalDateTime r_submitDate = LocalDateTime.now();
        String r_log = judgeResultVO.getExtraInfo();
        int r_state = (r_result.equals("ACCEPT")) ? 1 : 0;
        switch (submitResp.getLanguage()) {
            case "C":
                r_lang = 1;
                break;
            case "C_PLUS_PLUS":
                r_lang = 2;
                break;
            case "PYTHON":
                r_lang = 3;
                break;
            case "JAVA":
                r_lang = 4;
                break;
            default:
                break;
        }
        int r_test_case_total_num = judgeResultVO.getTotalNum();
        int r_test_case_access_num = judgeResultVO.getAccessNum();
        String r_submit_code = submitResp.getSubmissionCode();
        Records records = new Records(null, r_user_id, r_question_id, r_lang,
                r_result, r_execTime, r_memory, r_submitDate,
                r_log, r_state, r_test_case_total_num, r_test_case_access_num, r_submit_code);
        try {
            recordsMapper.insert(records);
        } catch (Exception e) {
            System.err.println(e);
            Resp.fail("题目提交失败！");
        }

        // 5. 返回 view object 给前端进行渲染
        return Resp.success(judgeResultVO);
    }

    /*
     * @Author YFMan
     * @Description 将 List<TestCase> 转换为 List<TestCaseDTO>
     * @Date 2023/4/24 11:05
     * @Param [testCaseList]
     * @return java.util.List<com.example.server.dto.Judgement.TestCaseDTO>
     **/
    private static List<TestCaseDTO> convertTestCaseToDTO(List<TestCase> testCaseList) {
        List<TestCaseDTO> dtoList = new ArrayList<>();
        for (TestCase testCase : testCaseList) {
            TestCaseDTO dto = new TestCaseDTO();
            dto.setStdIn(testCase.getInputPath());
            dto.setExpectedStdOut(testCase.getOutputPath());
            dtoList.add(dto);
        }
        return dtoList;
    }

    /*
     * @Author YFMan
     * @Description 远程调用 JudgeHost
     * @Date 2023/4/24 21:29
     * @Param [judgeResp]
     * @return java.lang.String
     **/
    private String runJudge(JudgeResp judgeResp) throws UnirestException, JsonProcessingException {

        // 远程调用 Judge-Host
        WebClient webClient = WebClient.create(baseURL);
        Mono<String> responseMono = webClient.post()
                .uri(JUDGE_URL)
                .header(ACCESS_TOKEN_KEY, getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(Mono.just(judgeResp), JudgeResp.class)
                .retrieve()
                .bodyToMono(String.class);
        return responseMono.block();
    }

    /*
     * @Author YFMan
     * @Description 对 JudgeHost 返回的结果进行分析，得到 view object
     * @Date 2023/4/24 21:32
     * @Param [judgeResult]
     * @return com.example.server.vo.JudgeResultVO
     **/
    private JudgeResultVO analysisJudgeResult(JudgeResult judgeResult) {
        // 执行对 Judge-Host 返回结果的分析
        JudgeResultAnalysisUtil analysisUtil = new JudgeResultAnalysisUtil(judgeResult);
        analysisUtil.executeAnalysis();

        // 创建 view object
        JudgeResultVO judgeResultVO = new JudgeResultVO();
        // 设置 TestCase 的总数量
        judgeResultVO.setTotalNum(totalSolutionsNum);
        judgeResultVO.setAccessNum(analysisUtil.getAccessNum());
        judgeResultVO.setJudgeCondition(analysisUtil.getJudgeCondition());
        judgeResultVO.setTimeCost(analysisUtil.getTimeCost());
        judgeResultVO.setMemoryCost(analysisUtil.getMemoryCost());
        judgeResultVO.setExtraInfo(analysisUtil.getExtraInfo());
        return judgeResultVO;
    }

    /*
     * @Author YFMan
     * @Description 生成访问 JudgeHost 的 Token
     * @Date 2023/4/24 21:28
     * @Param
     * @return
     **/
    public String getAccessToken() throws UnirestException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post(baseURL + TOKEN_URL)
                .header("Content-Type", "application/json")
                .body("{\r\n    \"userId\": \"" + userId + "\",\r\n    \"userSecret\": \"" + userSecret + "\"\r\n}")
                .asString();
        JSONObject jsonObject = new JSONObject(response.getBody());
        return jsonObject.getString("accessToken");
    }
}
