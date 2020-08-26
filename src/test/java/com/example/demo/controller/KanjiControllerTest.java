package com.example.demo.controller;

import com.example.demo.entity.Kanji;

import com.example.demo.repository.KanjiRepository;

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
public class KanjiControllerTest {

    @Autowired
    KanjiRepository kanjiRepository;

    @Test
    @Rollback(false)
    @Order(1)
    public void testCreateKanji() {
        Kanji savedKanji = new Kanji();
        savedKanji.setImage("Test Image");
        savedKanji.setKanji("Test Kanji");
        savedKanji.setKanji_meaning("Test Kanji Meaning");
        savedKanji.setKun_furigana("Test Kun Furigana");
        savedKanji.setKunyomi("Test Kunyomi");
        savedKanji.setOn_furigana("Test On Furigana");
        savedKanji.setOnyomi("Test Onyomi");
        savedKanji.setSino_vietnamese("Test Sino Vietnamese");
        savedKanji.setLesson_id((long) 100);

        kanjiRepository.save(savedKanji);

        assertThat(savedKanji.getImage()).isEqualTo("Test Image");
        assertThat(savedKanji.getKanji()).isEqualTo("Test Kanji");
        assertThat(savedKanji.getKanji_meaning()).isEqualTo("Test Kanji Meaning");
        assertThat(savedKanji.getKun_furigana()).isEqualTo("Test Kun Furigana");
        assertThat(savedKanji.getKunyomi()).isEqualTo("Test Kunyomi");
        assertThat(savedKanji.getOn_furigana()).isEqualTo("Test On Furigana");
        assertThat(savedKanji.getOnyomi()).isEqualTo("Test Onyomi");
        assertThat(savedKanji.getSino_vietnamese()).isEqualTo("Test Sino Vietnamese");
        assertThat(savedKanji.getLesson_id()).isGreaterThan(1);

    }

    @Test
    @Order(2)
    public void testFindByKanji() {
        Kanji findKanji = kanjiRepository.findByKanji("Test Kanji");
        assertThat(findKanji.getKanji()).isEqualTo("Test Kanji");
    }

    @Test
    @Order(3)
    public void testListKanjis() {
        List<Kanji> kanjis = kanjiRepository.findAll();
        assertThat(kanjis).size().isGreaterThan(0);
    }

    @Test
    @Rollback(false)
    @Order(4)
    public void testUpdateKanji() {
        Kanji kanji = kanjiRepository.findByKanji("Test Kanji");

        kanji.setImage("Update image");

        kanjiRepository.save(kanji);

        Kanji updateKanji = kanjiRepository.findByKanji("Test Kanji");

        assertThat(updateKanji.getImage()).isEqualTo("Update image");
    }

    @Test
    @Rollback(false)
    @Order(5)
    public void testDeleteKanji() {
        Kanji kanji = kanjiRepository.findByKanji("Test Kanji");

        kanjiRepository.deleteById(kanji.getId());

        Kanji deletedKanji = kanjiRepository.findByKanji("Test Kanji");

        assertThat(deletedKanji).isNull();
    }
}
