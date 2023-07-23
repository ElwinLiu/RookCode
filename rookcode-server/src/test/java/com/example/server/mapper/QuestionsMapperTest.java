package com.example.server.mapper;

import com.example.server.pojo.Questions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionsMapperTest {
    @Autowired(required = false)
    QuestionsMapper questionsMapper;

    @Test
    void saveQuestionReturnId() {
        Questions questions = new Questions();
        questions.setDifficulty(1);
        questions.setTitle("test title");
        questions.setContent("test content");
        questionsMapper.saveQuestionReturnId(questions);
        System.out.println(questions.getId());
    }
}
