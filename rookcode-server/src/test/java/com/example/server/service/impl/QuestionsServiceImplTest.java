package com.example.server.service.impl;

import com.example.server.dto.Question.QuestionTagsDTO;
import com.example.server.dto.Question.UpdateDifficultyDTO;
import com.example.server.dto.Question.UpdateTagsRequest;
import com.example.server.dto.Resp;
import com.example.server.pojo.QuestionTags;
import com.example.server.pojo.Questions;
import com.example.server.service.IQuestionsService;
import com.mysql.cj.util.DnsSrv;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionsServiceImplTest {
    @Autowired
    IQuestionsService questionsService;

    @Test
    void publishQuestion() {
        QuestionTagsDTO questionTagsDTO = new QuestionTagsDTO();
        questionTagsDTO.setTitle("test tile");
        questionTagsDTO.setContent("test content");
        questionTagsDTO.setDifficulty(1);
        List<String> tags = new ArrayList<>();
        tags.add("Shell");
        tags.add("二分查找");
        tags.add("二叉搜索树");
        questionTagsDTO.setTags(tags);
        Resp<Integer> resp = questionsService.publishQuestion(questionTagsDTO);
        System.out.println(resp);
    }

    @Test
    void updateQuestion() {
        QuestionTagsDTO questionTagsDTO = new QuestionTagsDTO();
        questionTagsDTO.setId(72);

        questionTagsDTO.setTitle("test update tile");
        questionTagsDTO.setContent("test update content");
        questionTagsDTO.setDifficulty(2);

        List<String> tags = new ArrayList<>();
        tags.add("Shell");
        tags.add("二分查找");
        tags.add("二叉搜索树");

        questionTagsDTO.setTags(tags);

        Resp<String> resp = questionsService.updateQuestion(questionTagsDTO);
        System.out.println(resp);
    }

    @Test
    void deleteQuestion() {
        for(int i=57;i<=68;i++){
            Resp<String> resp = questionsService.deleteQuestion(i);
            System.out.println(resp);
        }

    }

    @Test
    void updateQuestionDifficulty() {
        UpdateDifficultyDTO updateDifficultyDTO = new UpdateDifficultyDTO();
        updateDifficultyDTO.setQuestionId(85);
        updateDifficultyDTO.setDifficulty(2);
        Resp<String> resp = questionsService.updateQuestionDifficulty(updateDifficultyDTO);
        System.out.println(resp);
    }

    @Test
    void updateQuestionTags() {
        UpdateTagsRequest updateTagsRequest = new UpdateTagsRequest();
        updateTagsRequest.setQuestionId(85);
        List<String> tags = new ArrayList<>();
        tags.add("二叉树");
        tags.add("交互");
        updateTagsRequest.setTags(tags);
        Resp<String> resp = questionsService.updateQuestionTags(updateTagsRequest);
        System.out.println(resp);
    }
}
