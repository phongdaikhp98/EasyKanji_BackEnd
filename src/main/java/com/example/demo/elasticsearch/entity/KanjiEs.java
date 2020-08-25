package com.example.demo.elasticsearch.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Document(indexName = "ezkanji_index", type ="_doc")
public class KanjiEs {
    @Id
    @GeneratedValue
    private String id;
    private String kanji;
    private String sinoVietnamese;
    private String kanjiMeaning;
    private String onyomi;
    private String kunyomi;
    private String level;

}
