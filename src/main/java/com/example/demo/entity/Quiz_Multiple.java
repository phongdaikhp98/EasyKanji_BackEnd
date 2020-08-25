package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "quiz_multiple")
public class Quiz_Multiple {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Id auto increase upon create new")
    private Long id;

    @Column(insertable = true, updatable = true, nullable = false)
    private String question;


    @Column(insertable = true, updatable = true, nullable = false)
    private String answerA;

    @Column(insertable = true, updatable = true, nullable = false)
    private String answerB;

    @Column(insertable = true, updatable = true, nullable = false)
    private String answerC;

    @Column(insertable = true, updatable = true, nullable = false)
    private String answerD;

    @Column(insertable = true, updatable = true, nullable = false)
    private String correctAnswer;

    @Column(insertable = true, updatable = true)
    private Long lesson_id;

    @Column(insertable = true, updatable = true)
    private Long level_id;

//    @ManyToOne
//    @JoinColumn(name="lesson_id")
//    @JsonIgnore
//    private Lesson lesson;
//
//    @ManyToOne
//    @JoinColumn(name="level_id")
//    @JsonIgnore
//    private Level level;
}
