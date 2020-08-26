package com.example.demo.controller;

import com.example.demo.entity.Kanji;
import com.example.demo.entity.Quiz_Multiple;
import com.example.demo.repository.KanjiRepository;
import com.example.demo.repository.QuizMultipleRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuizMultipleControllerTest {
    @Autowired
    QuizMultipleRepository quizMultipleRepository;

    @Test
    @Rollback(false)
    @Order(1)
    public void testCreateQuizMultiple() {
        Quiz_Multiple quizMultiple = new Quiz_Multiple();
        quizMultiple.setQuestion("Test Question");
        quizMultiple.setAnswerA("Test AnswerA");
        quizMultiple.setAnswerB("Test AnswerB");
        quizMultiple.setAnswerC("Test AnswerC");
        quizMultiple.setAnswerD("Test AnswerD");
        quizMultiple.setCorrectAnswer("Test Answer Correct");
        quizMultiple.setLevel_id((long) 10);

        quizMultipleRepository.save(quizMultiple);

        assertThat(quizMultiple.getQuestion()).isEqualTo("Test Question");
        assertThat(quizMultiple.getAnswerA()).isEqualTo("Test AnswerA");
        assertThat(quizMultiple.getAnswerB()).isEqualTo("Test AnswerB");
        assertThat(quizMultiple.getAnswerC()).isEqualTo("Test AnswerC");
        assertThat(quizMultiple.getAnswerD()).isEqualTo("Test AnswerD");
        assertThat(quizMultiple.getCorrectAnswer()).isEqualTo("Test Answer Correct");
        assertThat(quizMultiple.getLevel_id()).isGreaterThan(0);

    }

    @Test
    @Order(2)
    public void testFindQuizMultipleByLevelID() {
        Quiz_Multiple findQuizMultiple = quizMultipleRepository.findByQuestion("Test Question");
        assertThat(findQuizMultiple.getLevel_id()).isGreaterThan(0);
    }

    @Test
    @Order(3)
    public void testListQuizMultiple() {
        List<Quiz_Multiple> quiz_multiples = quizMultipleRepository.findAll();
        assertThat(quiz_multiples).size().isGreaterThan(0);
    }

    @Test
    @Rollback(false)
    @Order(4)
    public void testUpdateQuizMultiple() {
        Quiz_Multiple quizMultiple = quizMultipleRepository.findByQuestion("Test Question");

        quizMultiple.setQuestion("Update question");
        quizMultiple.setAnswerA("Update answerA");
        quizMultiple.setAnswerB("Update answerB");
        quizMultiple.setAnswerC("Update answerC");
        quizMultiple.setAnswerD("Update answerD");
        quizMultiple.setCorrectAnswer("Update correct answer");


        quizMultipleRepository.save(quizMultiple);

        Quiz_Multiple updateQuizMultiple = quizMultipleRepository.findByQuestion("Update question");

        assertThat(updateQuizMultiple.getQuestion()).isEqualTo("Update question");
        assertThat(updateQuizMultiple.getAnswerA()).isEqualTo("Update answerA");
        assertThat(updateQuizMultiple.getAnswerB()).isEqualTo("Update answerB");
        assertThat(updateQuizMultiple.getAnswerC()).isEqualTo("Update answerC");
        assertThat(updateQuizMultiple.getAnswerD()).isEqualTo("Update answerD");
        assertThat(updateQuizMultiple.getCorrectAnswer()).isEqualTo("Update correct answer");
    }

    @Test
    @Rollback(false)
    @Order(5)
    public void testDeleteQuizMultiple() {
        Quiz_Multiple updateQuizMultiple = quizMultipleRepository.findByQuestion("Update question");

        quizMultipleRepository.deleteById(updateQuizMultiple.getId());

        Quiz_Multiple deletedQuizMultiple = quizMultipleRepository.findByQuestion("Update question");

        assertThat(deletedQuizMultiple).isNull();
    }
}
