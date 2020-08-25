package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "lesson")
public class  Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Id auto increase upon create new")
    private Long id;

    @Column(insertable = true, updatable = true, nullable = false)
    private String name;

    @Column(insertable = true, updatable = true)
    private Long level_id;

//    @ManyToOne
//    @JoinColumn(name="level_id")
//    @JsonIgnore
//    private Level level;

//    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @JsonIgnore
//    private List<Kanji> kanjis;

}
