package com.example.demo.controller;

import com.example.demo.entity.Vocabulary;
import com.example.demo.entity.customEntity.VocabByKanji;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.DTO.ApiResDTO;
import com.example.demo.repository.VocabularyRepository;
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
@Api(tags = "Vocabulary Rest Controller")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VocabularyController {
    @Autowired
    VocabularyRepository vocabularyRepository;

    @GetMapping("vocabs")
    @ApiOperation(value = "Get all vocabulary")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO getAllVocabulary(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        page = page -1;
        if(page < 0) {
            page = 0;
        }
        if(size > 10) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page,size);
        Page<Vocabulary> vocabularies = vocabularyRepository.findVocabularyPaging(pageable);
        if (vocabularies.isEmpty()) {
            return ApiResDTO.fail(null, "Don't have vocabularies");
        }
        return ApiResDTO.success(vocabularies, "Get list vocabularies successfully!");
    }

    @GetMapping("/vocabs/{id}")
    @ApiOperation(value = "Get vocab by ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO getVocabularyById(@PathVariable(value = "id") Long vocabID) {

        Vocabulary vocabulary = vocabularyRepository.findById(vocabID).orElse(null);
        if(vocabulary == null){
            return ApiResDTO.fail(null,"Vocabulary doesn't exist: " + vocabID);
        }

        return ApiResDTO.success(vocabulary,"Get vocabulary by ID successfully!");
    }

    @PostMapping("/vocabs")
    @ApiOperation(value = "Create new vocab")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public Vocabulary createVocabulary(@Valid @RequestBody Vocabulary vocabulary) {
        return vocabularyRepository.save(vocabulary);
    }

    @PutMapping("/vocabs/{id}")
    @ApiOperation(value = "Update vocab by ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO updateVocabulary(@PathVariable(value = "id") Long vocabularyID,
                                      @Valid @RequestBody Vocabulary vocabDetails) {
        Vocabulary vocabulary = vocabularyRepository.findById(vocabularyID).orElse(null);
        if(vocabulary == null){
            return ApiResDTO.fail(null,"Vocabulary doesn't exist: " + vocabularyID);
        }
        vocabulary.setKanji_vocab(vocabDetails.getKanji_vocab());
        vocabulary.setHiragana(vocabDetails.getHiragana());
        vocabulary.setVocab_meaning(vocabDetails.getVocab_meaning());
        vocabulary.setKanji_id(vocabDetails.getKanji_id());
        vocabularyRepository.save(vocabulary);

        return ApiResDTO.success(vocabulary,"Update vocabulary successfully!");
    }

    @DeleteMapping("/vocabs/{id}")
    @ApiOperation(value = "Delete vocab by ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO deleteLVocabulary(@PathVariable(value = "id") Long vocabId) {
        Vocabulary vocabulary = vocabularyRepository.findById(vocabId).orElse(null);
        if(vocabulary == null){
            return ApiResDTO.success(null,"Vocabulary doesn't exist: " + vocabId);
        }

        vocabularyRepository.delete(vocabulary);
        return ApiResDTO.success(null, "Delete vocabulary successfullly!");
    }


    @GetMapping("kanji/{id}/vocabs")
    public ResponseEntity<List<VocabByKanji>> getAllVocabByKanjiID(@PathVariable(value = "id") Long kanjiID)
            {
        List<VocabByKanji> kanjis =  vocabularyRepository.findVocab(kanjiID);
        if (kanjis.isEmpty()) {
            new ResourceNotFoundException("Don't have vocab in this kanji: " + kanjiID);
        }
        return new ResponseEntity<>(kanjis, HttpStatus.OK);
    }

    @GetMapping("lesson/{id}/vocabs")
    public ResponseEntity<List<VocabByKanji>> getAllVocabByLessonID(@PathVariable(value = "id") Long lessonID)
            {
        List<VocabByKanji> kanjis =  vocabularyRepository.findVocabByLesson(lessonID);
        if (kanjis.isEmpty()) {
            new ResourceNotFoundException("Don't have vocab in this lesson: " + lessonID);
        }
        return new ResponseEntity<>(kanjis, HttpStatus.OK);
    }
}
