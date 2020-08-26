package com.example.demo.controller;

import com.example.demo.entity.Lesson;
import com.example.demo.repository.LessonRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(SpringRunner.class)
//@WebMvcTest(LessonController.class)
//@ContextConfiguration(classes= App.class)
//@ProfileValueSourceConfiguration(LessonController.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LessonControllerTest {

//    @Autowired
//    MockMvc mockMvc;

    @Autowired
    LessonRepository lessonRepository;

    @Test
    @Rollback(false)
    @Order(1)
    public void testCreateLesson() {
        Lesson savedLesson = new Lesson();
        savedLesson.setName("Lesson 100");
        savedLesson.setLevel_id((long) 4);
        lessonRepository.save(savedLesson);

        assertThat(savedLesson.getName()).isEqualTo("Lesson 100");
        assertThat(savedLesson.getLevel_id()).isGreaterThan(1);
    }

    @Test
    @Order(2)
    public void testFindLessontByName() {
        Lesson findLesson = lessonRepository.findByName("Lesson 100");
        assertThat(findLesson.getName()).isEqualTo("Lesson 100");
    }

    @Test
    @Order(3)
    public void testListLessons() {
        List<Lesson> lessons = lessonRepository.findAll();
        assertThat(lessons).size().isGreaterThan(0);
    }

    @Test
    @Rollback(false)
    @Order(4)
    public void testUpdateLesson() {
        Lesson lesson = lessonRepository.findByName("Lesson 100");
        lesson.setLevel_id((long) 10);

        lessonRepository.save(lesson);

        Lesson updateLesson = lessonRepository.findByName("Lesson 100");

        assertThat(updateLesson.getName()).isEqualTo("Lesson 100");
    }

    @Test
    @Rollback(false)
    @Order(5)
    public void testDeleteLesson() {
        Lesson lesson = lessonRepository.findByName("Lesson 100");

        lessonRepository.deleteById(lesson.getId());

        Lesson deletedLesson = lessonRepository.findByName("Lesson 100");

        assertThat(deletedLesson).isNull();
    }


}
