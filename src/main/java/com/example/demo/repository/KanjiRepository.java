package com.example.demo.repository;

import com.example.demo.entity.Kanji;
import com.example.demo.entity.Level;
import com.example.demo.entity.customEntity.KanjiAndSinoOnly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KanjiRepository extends JpaRepository<Kanji, Long> {

    @Query(value = "SELECT * from kanji where kanji.lesson_id = ?1", nativeQuery = true)
    List<Kanji> findAllByLessonId(@Param("lesson_id")Long lessonID);

    @Query(value ="Select id,kanji,sino_vietnamese from kanji where kanji.lesson_id = ?1" , nativeQuery = true)
    List<KanjiAndSinoOnly> findKanji(@Param("id")Long lessonID);

    @Query(value ="SELECT kanji,sino_vietnamese FROM ((kanji inner join lesson on kanji.lesson_id = lesson.id ) inner join level on lesson.level_id  = level.id) where level_id = ?1" , nativeQuery = true)
    List<KanjiAndSinoOnly> findKanjiByLevel(@Param("id")Long levelID);

    @Query(value = "SELECT * from kanji ", nativeQuery = true)
    Page<Kanji> findKanjiPaging(Pageable pageable);

    Kanji findByKanji(String kanji);
}
