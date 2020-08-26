package com.example.demo.repository;

import com.example.demo.entity.Kanji;
import com.example.demo.entity.Quiz_Multiple;
import com.example.demo.entity.Vocabulary;
import com.example.demo.entity.customEntity.QuizMultiByLesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizMultipleRepository extends JpaRepository<Quiz_Multiple, Long> {
    @Query(value = "SELECT id,answerA,answerB,answerC,answerD,correctAnswer,question FROM quiz_multiple where quiz_multiple.lesson_id = ?1", nativeQuery = true)
    List<QuizMultiByLesson> findQuizByLessonId(@Param("lesson_id")Long lessonID);

    @Query(value = "SELECT id,answerA,answerB,answerC,answerD,correctAnswer,question FROM quiz_multiple where quiz_multiple.level_id = ?1", nativeQuery = true)
    List<QuizMultiByLesson> findQuizByLevelId(@Param("level_id")Long levelID);

    @Query(value = "SELECT * from quiz_multiple ", nativeQuery = true)
    Page<Quiz_Multiple> findQuizMultipkePaging(Pageable pageable);

    Quiz_Multiple findByQuestion(String question);
}
