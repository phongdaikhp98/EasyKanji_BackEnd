package com.example.demo.controller;

import com.example.demo.entity.Lesson;
import com.example.demo.entity.customEntity.LessonByLevel;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.DTO.ApiResDTO;
import com.example.demo.repository.LessonRepository;
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
@Api(tags = "Lesson Rest Controller")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {
    @Autowired
    LessonRepository lessonRepository;


    @GetMapping("/lessons")
    @ApiOperation(value = "Get all lessons")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO getAllLesson(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        page = page -1;
        if(page < 0) {
            page = 1;
        }
        if(size > 100) {
            size = 100;
        }
        Pageable pageable = PageRequest.of(page,size);
        Page<Lesson> lessons = lessonRepository.findLessonPaging(pageable);
        if (lessons.isEmpty()) {
            return ApiResDTO.fail(null, "Don't have lessons");
        }
         return ApiResDTO.success(lessons, "Get list lesson successfully!");
    }

    @GetMapping("/lessons/{id}")
    @ApiOperation(value = "Get lesson by ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO getLessonById(@PathVariable(value = "id") Long lessonID) {
        Lesson lesson = lessonRepository.findById(lessonID).orElse(null);
         if(lesson == null){
             return ApiResDTO.fail(null,"Lesson doesn't exist: " + lessonID);
         }

         return ApiResDTO.success(lesson,"Get lesson by ID successfully!");
    }

    @PostMapping("/lessons")
    @ApiOperation(value = "Create new lesson")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO createLesson(@Valid @RequestBody Lesson lesson) {
        try {
            return ApiResDTO.success(lessonRepository.save(lesson), "Add lesson successfully");
        }catch (Exception e){
            e.printStackTrace();
            return ApiResDTO.fail(null, "Add lesson fail");
        }
    }

    @PutMapping("/lessons/{id}")
    @ApiOperation(value = "Update lesson by ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO updateLesson(@PathVariable(value = "id") Long lessonID,
                                  @Valid @RequestBody Lesson lessonDetails)  {
        Lesson lesson = lessonRepository.findById(lessonID).orElse(null);
        if(lesson == null){
            return ApiResDTO.fail(null,"Lesson doesn't exist: " + lessonID);
        }
        lesson.setName(lessonDetails.getName());
        lesson.setLevel_id(lessonDetails.getLevel_id());
        lessonRepository.save(lesson);

        return ApiResDTO.success(lesson,"Update Lesson successfully!");
    }


    @DeleteMapping("/lessons/{id}")
    @ApiOperation(value = "Delete lesson by ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO deleteLesson(@PathVariable(value = "id") Long lessonId) {

        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);
         if(lesson == null){
             return ApiResDTO.fail(null,"Lesson doesn't exist: " + lessonId);
         }

        lessonRepository.delete(lesson);
         return ApiResDTO.success(null, "Delete lesson successfullly!");
    }


    @GetMapping("level/{id}/lessons")
    @ApiOperation(value = "Get lesson by level ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ResponseEntity<List<LessonByLevel>> getAllLessonByLevelID(@PathVariable(value = "id") Long levelId) {
        List<LessonByLevel> lesson =  lessonRepository.findLesson(levelId);
        if (lesson.isEmpty()) {
            new ResourceNotFoundException("Don't have lesson in this level: " + levelId);
        }
        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }
}
