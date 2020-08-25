package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "vocabulary")
public class Vocabulary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Id auto increase upon create new")
    private Long id;

    @Column(insertable = true, updatable = true)
    private String kanji_vocab;

    @Column(insertable = true, updatable = true)
    private String hiragana;

    @Column(insertable = true, updatable = true)
    private String vocab_meaning;

    @Column(insertable = true, updatable = true)
    private Long kanji_id;

//    @ManyToOne
//    @JoinColumn(name="kanji_id")
//    @JsonIgnore
//    private Kanji kanji;
}
