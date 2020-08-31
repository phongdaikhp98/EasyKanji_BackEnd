package com.example.demo.controller;

import com.example.demo.elasticsearch.entity.KanjiEs;
import com.example.demo.elasticsearch.repository.KanjiEsRepository;
import com.example.demo.entity.Kanji;
import com.example.demo.entity.customEntity.KanjiAndSinoOnly;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.DTO.ApiResDTO;
import com.example.demo.DTO.KanjiSearchDTO;
import com.example.demo.repository.KanjiRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Api(tags = "Kanji Rest Controller")
@CrossOrigin(origins = "*", maxAge = 3600)
public class KanjiController {
    @Autowired
    KanjiRepository kanjiRepository;

    @Autowired
    KanjiEsRepository kanjiEsRepository;

    @GetMapping("kanjis")
    @ApiOperation(value = "Get all kanjis")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO getAllKanji(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        page = page - 1;
        if(page < 0) {
            page = 0;
        }
        if(size > 100) {
            size = 100;
        }
        Pageable pageable = PageRequest.of(page,size);
        Page<Kanji> kanjis = kanjiRepository.findKanjiPaging(pageable);
        if (kanjis.isEmpty()) {
            return ApiResDTO.fail(null, "Don't have kanjis");
        }
        return ApiResDTO.success(kanjis, "Get list kanji successfully!");
    }

    @GetMapping("/kanjis/{id}")
    @ApiOperation(value = "Get kanji by ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO getKanjiById(@PathVariable(value = "id") Long kanjiID) {

        Kanji kanji = kanjiRepository.findById(kanjiID).orElse(null);
        if(kanji == null){
            return ApiResDTO.fail(null,"User doesn't exist: " + kanjiID);
        }

        return ApiResDTO.success(kanji,"Get kanji by ID successfully!");
    }

    @PostMapping("/kanjis")
    @ApiOperation(value = "Create new kanji")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO createKanji(@Valid @RequestBody Kanji kanji) {
        try {
            return ApiResDTO.success(kanjiRepository.save(kanji), "Add kanji successfully");
        }catch (Exception e){
            e.printStackTrace();
            return ApiResDTO.fail(null, "Add kanji fail");
        }
    }

    @PutMapping("/kanjis/{id}")
    @ApiOperation(value = "Update kanji by ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO updateKanji(@PathVariable(value = "id") Long kanjiID,
                                 @Valid @RequestBody Kanji kanjiDetails){
        Kanji kanji = kanjiRepository.findById(kanjiID).orElse(null);
        if(kanji == null){
            return ApiResDTO.fail(null,"Kanji doesn't exist: " + kanjiID);
        }
        kanji.setImage(kanjiDetails.getImage());
        kanji.setSino_vietnamese(kanjiDetails.getSino_vietnamese());
        kanji.setKanji_meaning(kanjiDetails.getKanji_meaning());
        kanji.setOnyomi(kanjiDetails.getOnyomi());
        kanji.setOn_furigana(kanjiDetails.getOn_furigana());
        kanji.setKunyomi(kanjiDetails.getKunyomi());
        kanji.setKun_furigana(kanjiDetails.getKun_furigana());
        kanji.setLesson_id(kanjiDetails.getLesson_id());
        kanjiRepository.save(kanji);

        return ApiResDTO.success(kanji,"Update kanji successfully!");
    }

    @DeleteMapping("/kanjis/{id}")
    @ApiOperation(value = "Delete kanji by ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ApiResDTO deleteLKanji(@PathVariable(value = "id") Long kanjiId) {

        Kanji kanji = kanjiRepository.findById(kanjiId).orElse(null);
        if(kanji == null){
            return ApiResDTO.fail(null,"Kanji doesn't exist: " + kanjiId);
        }

        kanjiRepository.delete(kanji);
        return ApiResDTO.success(null, "Delete kanji successfullly!");
    }

    @GetMapping("lessons/{id}/kanjis")
    @ApiOperation(value = "Get kanji by lessonID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ResponseEntity<List<KanjiAndSinoOnly>> getAllKanjiByLessonID(@PathVariable(value = "id") Long lessonID)
            throws ResourceNotFoundException {
        List<KanjiAndSinoOnly> kanjis = kanjiRepository.findKanji(lessonID);
        if (kanjis.isEmpty()) {
            new ResourceNotFoundException("Don't have kanji in lesson: " + lessonID);
        }
        return new ResponseEntity<>(kanjis, HttpStatus.OK);
    }

    @GetMapping("level/{id}/kanjis")
    @ApiOperation(value = "Get kanji by levelID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ResponseEntity<List<KanjiAndSinoOnly>> getAllKanjiByLevelID(@PathVariable(value = "id") Long levelID)
            throws ResourceNotFoundException {
        List<KanjiAndSinoOnly> kanjis =  kanjiRepository.findKanjiByLevel(levelID);
        if (kanjis.isEmpty()) {
            new ResourceNotFoundException("Don't have kanji in lesson: " + levelID);
        }
        return new ResponseEntity<>(kanjis, HttpStatus.OK);
    }

    @PostMapping("/search")
    @ApiOperation(value = "Search kanji by ElasticSearch")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ResponseEntity<Page<KanjiEs>> searchKanji(@Valid @RequestBody KanjiSearchDTO kanjiSearchDTO) {
        String keyword = kanjiSearchDTO.getKeyword();
        if(keyword.equals(null) || keyword.equals("")){
            new ResponseEntity<>("String cann't be null or empty", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(keyword.matches(".*\\w.*"))
        {
            new ResponseEntity<>("String cann't be all white space", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//        MojiConverter converter = new MojiConverter();
        for(char c : keyword.toCharArray()){
            if(Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HIRAGANA ){
                boolQueryBuilder.must(QueryBuilders.matchQuery("kunyomi", keyword));
            }else if(Character.UnicodeBlock.of(c) == Character.UnicodeBlock.KATAKANA){
                boolQueryBuilder.must(QueryBuilders.matchQuery("onyomi", keyword));
            }else if(Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS){
                boolQueryBuilder.must(QueryBuilders.matchQuery("kanji", keyword));
            }else{
                boolQueryBuilder.must(QueryBuilders.matchQuery("sinoVietnamese", keyword).fuzziness(Fuzziness.ONE));
            }
        }

        NativeSearchQuery build = new NativeSearchQueryBuilder().withPageable(PageRequest.of(0,100))
                .withQuery(boolQueryBuilder).build();

        Page<KanjiEs> kanjis = kanjiEsRepository.search(build);

        if (kanjis.isEmpty()) {
            new ResourceNotFoundException("Don't have kanji");
        }
        return new ResponseEntity<>(kanjis, HttpStatus.OK);
    }


}
