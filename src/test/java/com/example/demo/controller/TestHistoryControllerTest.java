package com.example.demo.controller;

import com.example.demo.entity.Lesson;
import com.example.demo.entity.Test_History;
import com.example.demo.repository.TestHistoryRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestHistoryControllerTest {
    @Autowired
    TestHistoryRepository testHistoryRepository;

    @Test
    @Rollback(false)
    @Order(1)
    public void testSaveResult() {
        Test_History test_history = new Test_History();
        test_history.setTimeTaken((long) 700);
        test_history.setResultPoint((long) 100);
        test_history.setDateAttend("25/8/2020");
        test_history.setUser_id((long) 100);
        test_history.setLevel_id((long) 100);


        testHistoryRepository.save(test_history);

        assertThat(test_history.getTimeTaken()).isGreaterThan(1);
        assertThat(test_history.getResultPoint()).isGreaterThan(1);
        assertThat(test_history.getDateAttend()).isEqualTo("25/8/2020");
        assertThat(test_history.getUser_id()).isGreaterThan(1);
        assertThat(test_history.getLevel_id()).isGreaterThan(1);
    }
}
