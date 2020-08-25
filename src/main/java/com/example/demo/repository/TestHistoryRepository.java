package com.example.demo.repository;

import com.example.demo.entity.Test_History;
import com.example.demo.entity.customEntity.RankingByLevel;
import com.example.demo.entity.customEntity.TestHistoryByUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestHistoryRepository extends JpaRepository<Test_History, Long> {
    @Query(value ="Select resultPoint, dateAttend, timeTaken from test_history where user_id = ?1 and level_id = ?2 order by id desc" , nativeQuery = true)
    List<TestHistoryByUser> findTestHistory(@Param("id")Long userID, @Param("id")Long levelID);

    @Query(value ="SELECT resultPoint,timeTaken,username,dateAttend FROM ((test_history inner join users on test_history.user_id = users.id ) inner join level on test_history.level_id  = level.id) where level_id = ?1 order by resultPoint desc, timeTaken asc limit 10" , nativeQuery = true)
    List<RankingByLevel> getListRanking(@Param("id")Long userID);
}
