package com.example.demo.repository;

import com.example.demo.entity.Lesson;
import com.example.demo.entity.Level;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {
    @Query(value = "SELECT * from level ", nativeQuery = true)
    Page<Level> findLevelPaging(Pageable pageable);

}
