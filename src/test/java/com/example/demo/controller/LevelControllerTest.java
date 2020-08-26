package com.example.demo.controller;

import com.example.demo.entity.Lesson;
import com.example.demo.entity.Level;
import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.LevelRepository;
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
class LevelControllerTest {

    @Autowired
    LevelRepository levelRepository;

    @Test
    @Rollback(false)
    @Order(1)
    public void testCreateLevel() {
        Level savedLevel = new Level();
        savedLevel.setName("Level 4");
        savedLevel.setDescription("Test create level");
        levelRepository.save(savedLevel);

        assertThat(savedLevel.getName()).isEqualTo("Level 4");
        assertThat(savedLevel.getDescription()).isEqualTo("Test create level");
    }

    @Test
    @Order(2)
    public void testFindLevelByName() {
        Level findLevel = levelRepository.findByName("Level 4");
        assertThat(findLevel.getName()).isEqualTo("Level 4");
    }

    @Test
    @Order(3)
    public void testListLevels() {
        List<Level> levvels = levelRepository.findAll();
        assertThat(levvels).size().isGreaterThan(0);
    }

    @Test
    @Rollback(false)
    @Order(4)
    public void testUpdateLevel() {
        Level level = levelRepository.findByName("Level 4");
        level.setDescription("Update create level");

        levelRepository.save(level);

        Level updateLevel = levelRepository.findByName("Level 4");

        assertThat(updateLevel.getDescription()).isEqualTo("Update create level");
    }

    @Test
    @Rollback(false)
    @Order(5)
    public void testDeleteLevel() {
        Level level = levelRepository.findByName("Level 4");

        levelRepository.deleteById(level.getId());

        Level deleteLevel = levelRepository.findByName("Level 4");

        assertThat(deleteLevel).isNull();
    }
}
