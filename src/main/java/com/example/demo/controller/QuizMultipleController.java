package com.example.demo.controller;

import com.example.demo.entity.Quiz_Multiple;
import com.example.demo.entity.customEntity.QuizMultiByLesson;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.DTO.ApiResDTO;
import com.example.demo.repository.QuizMultipleRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Api(tags = "Quiz Multiple Choice Rest Controller")
@CrossOrigin(origins = "*", maxAge = 3600)
public class QuizMultipleController {
    @Autowired
    QuizMultipleRepository quizMultipleRepository;

    @GetMapping("/quizzesMultiple")
    @ApiOperation(value = "Get all quiz multiple")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO getAllQuizMultiple(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        page = page -1;
        if(page < 0) {
            page = 0;
        }
        if(size > 10) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page,size);
        Page<Quiz_Multiple> quiz_multiples = quizMultipleRepository.findQuizMultipkePaging(pageable);
        if (quiz_multiples.isEmpty()) {
            return ApiResDTO.fail(null, "Don't have quiz_multiples");
        }
        return ApiResDTO.success(quiz_multiples, "Get list quiz_multiples successfully!");
    }

    @GetMapping("/quizzesMultiple/{id}")
    @ApiOperation(value = "Get quiz multiple by ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO getQuizMultiplelById(@PathVariable(value = "id") Long quizMultipleID) {
        Quiz_Multiple quiz_multiple = quizMultipleRepository.findById(quizMultipleID).orElse(null);
        if(quiz_multiple == null){
            return ApiResDTO.fail(null,"Level doesn't exist: " + quizMultipleID);
        }

        return ApiResDTO.success(quiz_multiple,"Get quiz_multiple by ID successfully!");
    }

    @PostMapping("/quizzesMultiple")
    @ApiOperation(value = "Create new quiz multiple")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public Quiz_Multiple createQuizMultiple(@Valid @RequestBody Quiz_Multiple quizMultipleID) {
        return quizMultipleRepository.save(quizMultipleID);
    }

    @PutMapping("/quizzesMultiple/{id}")
    @ApiOperation(value = "Update quiz multiple by ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO updateQuizMultiple(@PathVariable(value = "id") Long quizMultipleID,
                                        @Valid @RequestBody Quiz_Multiple quizMultipleDetails) {
        Quiz_Multiple quiz_multiple = quizMultipleRepository.findById(quizMultipleID).orElse(null);
        if(quiz_multiple == null){
            return ApiResDTO.fail(null,"Quiz Multiple doesn't exist: " + quizMultipleID);
        }
        quiz_multiple.setQuestion(quizMultipleDetails.getQuestion());
        quiz_multiple.setAnswerA(quizMultipleDetails.getAnswerA());
        quiz_multiple.setAnswerB(quizMultipleDetails.getAnswerB());
        quiz_multiple.setAnswerC(quizMultipleDetails.getAnswerC());
        quiz_multiple.setAnswerD(quizMultipleDetails.getAnswerD());
        quiz_multiple.setCorrectAnswer(quizMultipleDetails.getCorrectAnswer());

        quizMultipleRepository.save(quiz_multiple);

        return ApiResDTO.success(quiz_multiple,"Update quiz_multiple successfully!");
    }

    @DeleteMapping("/quizzesMultiple/{id}")
    @ApiOperation(value = "Delete quiz multiple by ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO deleteQuizMultiple(@PathVariable(value = "id") Long quizMultipleID) {
        Quiz_Multiple quiz_multiple = quizMultipleRepository.findById(quizMultipleID).orElse(null);
        if(quiz_multiple == null){
            return ApiResDTO.success(null,"Level doesn't exist: " + quizMultipleID);
        }

        quizMultipleRepository.delete(quiz_multiple);
        return ApiResDTO.success(null, "Delete level successfullly!");
    }

    @GetMapping("lesson/{id}/quizzesMultiple")
    @ApiOperation(value = "Get quiz multiple by lesson ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ResponseEntity<List<QuizMultiByLesson>> getQuizByLessonID(@PathVariable(value = "id") Long lessonID) {
        List<QuizMultiByLesson> quizzes =  quizMultipleRepository.findQuizByLessonId(lessonID);
        if (quizzes.isEmpty()) {
            new ResourceNotFoundException("Don't have quiz multiple in this lesson: " + lessonID);
        }
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    @GetMapping("level/{id}/quizzesMultiple")
    @ApiOperation(value = "Get quiz multiple by level ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ResponseEntity<List<QuizMultiByLesson>> getQuizByLevelID(@PathVariable(value = "id") Long levelID) {
        List<QuizMultiByLesson> quizzes =  quizMultipleRepository.findQuizByLevelId(levelID);
        if (quizzes.isEmpty()) {
            new ResourceNotFoundException("Don't have quiz multiple in this lesson: " + levelID);
        }
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }
}
