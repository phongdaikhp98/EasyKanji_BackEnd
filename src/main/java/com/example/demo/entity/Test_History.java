package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "test_history")
public class Test_History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(insertable = true, updatable = true)
    private Long resultPoint;

    @Column(insertable = true, updatable = true)
    private String dateAttend;

    @Column(insertable = true, updatable = true)
    private Long timeTaken;

    @Column(insertable = true, updatable = true)
    private Long user_id;

    @Column(insertable = true, updatable = true)
    private Long level_id;
}
