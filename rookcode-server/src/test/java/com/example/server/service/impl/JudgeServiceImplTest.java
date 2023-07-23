package com.example.server.service.impl;

import com.example.server.dto.Resp;
import com.example.server.dto.Record.SubmitResp;
import com.example.server.service.IJudgeService;
import com.example.server.vo.JudgeResultVO;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JudgeServiceImplTest {
    @Autowired
    IJudgeService judgeService;

    @Test
    void judgeQuestion() throws JsonProcessingException, UnirestException {
        SubmitResp submitResp = new SubmitResp();
        submitResp.setQuestionId(51);
        String submitCode = "#include<iostream>\n" +
                "using namespace std;\n" +
                "\n" +
                "int main() {\n" +
                "\tint a, b;\n" +
                "\tcin >> a >> b;\n" +
                "\tcout << a + b;\n" +
                "}";
        submitResp.setSubmissionCode(submitCode);
        submitResp.setLanguage("C_PLUS_PLUS");

        Resp<JudgeResultVO> judgeResultVOResp = judgeService.judgeQuestion(submitResp, "222");

        System.out.println(judgeResultVOResp);
    }
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Test
    void jsonTest() throws JsonProcessingException {
        String submitCode = "#include<iostream>\n" +
                "using namespace std;\n" +
                "\n" +
                "int main() {\n" +
                "\tint a, b;\n" +
                "\tcin >> a >> b;\n" +
                "\tcout << a + b;\n" +
                "}";
        String jsonStr = objectMapper.writeValueAsString(submitCode);
        System.out.println(jsonStr);
        String str = objectMapper.readValue(jsonStr,String.class);
        System.out.println(str);
    }

    @Test
    void  jsonTest2() throws JsonProcessingException {
        SubmitResp submitResp = new SubmitResp();
        submitResp.setQuestionId(51);
        String submitCode = "#include<iostream>\n" +
                "using namespace std;\n" +
                "\n" +
                "int main() {\n" +
                "\tint a, b;\n" +
                "\tcin >> a >> b;\n" +
                "\tcout << a + b;\n" +
                "}";
        submitResp.setSubmissionCode(submitCode);
        submitResp.setLanguage("C_PLUS_PLUS");

        String jsonStr = objectMapper.writeValueAsString(submitResp);
        System.out.println(jsonStr);

        SubmitResp submitResp1 = objectMapper.readValue(jsonStr,SubmitResp.class);
        System.out.println(submitResp1);
    }

    @Test
    void getTokenTest() throws UnirestException {
        String token = judgeService.getAccessToken();
        System.out.println(token);
    }
}
