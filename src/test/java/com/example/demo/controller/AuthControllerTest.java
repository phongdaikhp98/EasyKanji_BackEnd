package com.example.demo.controller;

import com.example.demo.entity.Kanji;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
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
public class AuthControllerTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @Rollback(false)
    @Order(1)
    public void testSignUpUser() {
        User testUser = new User();
        testUser.setEmail("Test Email");
        testUser.setPassword("Test Password");
        testUser.setUsername("Test Username");
        testUser.setAvatar("Test Avatar");
        testUser.setEnabled(true);


        userRepository.save(testUser);

        assertThat(testUser.getEmail()).isEqualTo("Test Email");
        assertThat(testUser.getPassword()).isEqualTo("Test Password");
        assertThat(testUser.getUsername()).isEqualTo("Test Username");
        assertThat(testUser.getAvatar()).isEqualTo("Test Avatar");
        assertThat(testUser.isEnabled()).isEqualTo(true);

    }

    @Test
    @Rollback(false)
    @Order(2)
    public void testFindByEmail() {
        User findUser = userRepository.findByEmail("Test Email").orElse(null);
        assertThat(findUser.getEmail()).isEqualTo("Test Email");
    }

    @Test
    @Order(3)
    public void testListUsers() {
        List<User> users = userRepository.findAll();
        assertThat(users).size().isGreaterThan(0);
    }

    @Test
    @Rollback(false)
    @Order(4)
    public void testUpdateUser() {
        User user = userRepository.findByEmail("Test Email").orElse(null);

        user.setPassword("Update password");
        user.setUsername("Update username");
        user.setAvatar("Update avatar");

        userRepository.save(user);

        User updateUser = userRepository.findByEmail("Test Email").orElse(null);

        assertThat(updateUser.getPassword()).isEqualTo("Update password");
        assertThat(updateUser.getUsername()).isEqualTo("Update username");
        assertThat(updateUser.getAvatar()).isEqualTo("Update avatar");

    }

    @Test
    @Rollback(false)
    @Order(5)
    public void testDeleteUser() {
        User user = userRepository.findByEmail("Test Email").orElse(null);

        userRepository.deleteById(user.getId());

        User deleteUser = userRepository.findByEmail("Test Email").orElse(null);

        assertThat(deleteUser).isNull();
    }

}
