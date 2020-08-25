package com.example.demo.controller;

import com.example.demo.entity.Test_History;
import com.example.demo.entity.customEntity.RankingByLevel;
import com.example.demo.entity.customEntity.TestHistoryByUser;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.DTO.TestResultDTO;
import com.example.demo.repository.TestHistoryRepository;
import com.example.demo.response.MessageResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TestHistoryController {
    @Autowired
    TestHistoryRepository testHistoryRepository;

    @GetMapping("test/{id1}/users/{id2}/levels")
    @ApiOperation(value = "Get list test history of user")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ResponseEntity<List<TestHistoryByUser>> getTestHistoryByUserID(@PathVariable(value = "id1") Long userID, @PathVariable(value = "id2") Long levelID)
            throws ResourceNotFoundException {
        List<TestHistoryByUser> tests =  testHistoryRepository.findTestHistory(userID, levelID);
        if (tests.isEmpty()) {
            new ResourceNotFoundException("Don't have test history of this user: " + userID);
        }
        return new ResponseEntity<>(tests, HttpStatus.OK);
    }

    @PostMapping("/test/save_result")
    @ApiOperation(value = "Save test  result of user")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ResponseEntity<?> saveResult(@Valid @RequestBody TestResultDTO testResultDTO){

        Long timeTaken = testResultDTO.getTimeTaken();
        Long resultPoint = testResultDTO.getResultPoint();
        String dateAttend = testResultDTO.getDateAttend();
        Long user_id = testResultDTO.getUser_id();
        Long level_id = testResultDTO.getLevel_id();

        Test_History test_history = new Test_History();

        test_history.setTimeTaken(timeTaken);
        test_history.setResultPoint(resultPoint);
        test_history.setDateAttend(dateAttend);
        test_history.setUser_id(user_id);
        test_history.setLevel_id(level_id);

        testHistoryRepository.save(test_history);

        return ResponseEntity.ok(new MessageResponse("Test result has been saved successfully!"));
    }

    @GetMapping("level/{id}/rankings")
    @ApiOperation(value = "Get list ranking of all user by level")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ResponseEntity<List<RankingByLevel>> getRankingByLevel(@PathVariable(value = "id") Long levelID)
            throws ResourceNotFoundException {
        List<RankingByLevel> tests =  testHistoryRepository.getListRanking(levelID);
        if (tests.isEmpty()) {
            new ResourceNotFoundException("Don't have ranking in this level: " + levelID);
        }
        return new ResponseEntity<>(tests, HttpStatus.OK);
    }
}
