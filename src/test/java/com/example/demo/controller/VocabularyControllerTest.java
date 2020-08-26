package com.example.demo.controller;

import com.example.demo.entity.Lesson;
import com.example.demo.entity.Vocabulary;
import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.VocabularyRepository;
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
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VocabularyControllerTest {

    @Autowired
    VocabularyRepository vocabularyRepository;

    @Test
    @Rollback(false)
    @Order(1)
    public void testCreateVocabulary() {
        Vocabulary savedVocabulary = new Vocabulary();
        savedVocabulary.setVocab_meaning("Test Vocab Meaning");
        savedVocabulary.setHiragana("Test Hiragana");
        savedVocabulary.setKanji_vocab("Test Kanji Vocab");
        savedVocabulary.setKanji_id((long) 200);

        vocabularyRepository.save(savedVocabulary);

        assertThat(savedVocabulary.getVocab_meaning()).isEqualTo("Test Vocab Meaning");
        assertThat(savedVocabulary.getHiragana()).isEqualTo("Test Hiragana");
        assertThat(savedVocabulary.getKanji_vocab()).isEqualTo("Test Kanji Vocab");
        assertThat(savedVocabulary.getKanji_id()).isGreaterThan(1);
    }

    @Test
    @Order(2)
    public void testFindVocabularyByHiragana() {
        Vocabulary findVocabulary = vocabularyRepository.findByHiragana("Test Hiragana");
        assertThat(findVocabulary.getHiragana()).isEqualTo("Test Hiragana");
    }

    @Test
    @Order(3)
    public void testListVocabularies() {
        List<Vocabulary> vocabularies = vocabularyRepository.findAll();
        assertThat(vocabularies).size().isGreaterThan(0);
    }

    @Test
    @Rollback(false)
    @Order(4)
    public void testUpdateVocabulary() {
        Vocabulary vocabularies = vocabularyRepository.findByHiragana("Test Hiragana");
        vocabularies.setKanji_vocab("Update Kanji Vocab");
        vocabularies.setHiragana("Update Hiragana");
        vocabularies.setVocab_meaning("Update Vocab Meaning");

        vocabularyRepository.save(vocabularies);

        Vocabulary updateVocabularyn = vocabularyRepository.findByHiragana("Update Hiragana");

        assertThat(updateVocabularyn.getKanji_vocab()).isEqualTo("Update Kanji Vocab");
        assertThat(updateVocabularyn.getHiragana()).isEqualTo("Update Hiragana");
        assertThat(updateVocabularyn.getVocab_meaning()).isEqualTo("Update Vocab Meaning");
    }

    @Test
    @Rollback(false)
    @Order(5)
    public void testDeleteVocabulary() {
        Vocabulary vocabulary = vocabularyRepository.findByHiragana("Update Hiragana");

        vocabularyRepository.deleteById(vocabulary.getId());

        Vocabulary deleteVocabulary = vocabularyRepository.findByHiragana("Update Hiragana");

        assertThat(deleteVocabulary).isNull();
    }
}
