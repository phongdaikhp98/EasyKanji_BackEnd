package com.example.demo.controller;

import com.example.demo.entity.Level;
import com.example.demo.DTO.ApiResDTO;
import com.example.demo.repository.LevelRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Api(tags = "Level Rest Controller")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LevelController {

    @Autowired
    LevelRepository levelRepository;

    @GetMapping("/levels")
    @ApiOperation(value = "Get all level")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO getAllLevel() {
        List<Level> levels = levelRepository.findAll();
        if (levels.isEmpty()) {
            return ApiResDTO.fail(null,"Don't have level" );
        }
        return ApiResDTO.success(levels,"Get list level successfully!");
    }

    @GetMapping("/levels/{id}")
    @ApiOperation(value = "Get level by ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO getLevelById(@PathVariable(value = "id") Long levelID) {
        Level level = levelRepository.findById(levelID).orElse(null);
        if(level == null){
            return ApiResDTO.fail(null,"Level doesn't exist: " + levelID);
        }

        return ApiResDTO.success(level,"Get level by ID successfully!");
    }

    @PostMapping("/levels")
    @ApiOperation(value = "Create new level")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO createLevel(@Valid @RequestBody Level level) {
        try {
            return ApiResDTO.success(levelRepository.save(level), "Add level successfully");
        }catch (Exception e){
            e.printStackTrace();
            return ApiResDTO.fail(null, "Add level fail");
        }    }

    @PutMapping("/levels/{id}")
    @ApiOperation(value = "Update level by ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO updateLevel(@PathVariable(value = "id") Long levelID,
                                 @Valid @RequestBody Level levelDetails) {
        Level level = levelRepository.findById(levelID).orElse(null);
        if(level == null){
            return ApiResDTO.fail(null,"Level doesn't exist: " + levelID);
        }
        level.setName(levelDetails.getName());
        level.setDescription(levelDetails.getDescription());
        levelRepository.save(level);

        return ApiResDTO.success(level,"Update level successfully!");
    }

    @DeleteMapping("/levels/{id}")
    @ApiOperation(value = "Delete level by ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO deleteLevel(@PathVariable(value = "id") Long levelId) {
        Level level = levelRepository.findById(levelId).orElse(null);
        if(level == null){
            return ApiResDTO.success(null,"Level doesn't exist: " + levelId);
        }

        levelRepository.delete(level);
        return ApiResDTO.success(null, "Delete level successfullly!");
    }
}
