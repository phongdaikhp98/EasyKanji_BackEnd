package com.example.demo.DTO;

import javax.validation.constraints.NotBlank;

public class TestResultDTO {
    @NotBlank
    private Long level_id;

    @NotBlank
    private Long timeTaken;

    @NotBlank
    private Long resultPoint;

    @NotBlank
    private String dateAttend;

    @NotBlank
    private Long user_id;

    public Long getLevel_id() {
        return level_id;
    }

    public void setLevel_id(Long level_id) {
        this.level_id = level_id;
    }

    public Long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public Long getResultPoint() {
        return resultPoint;
    }

    public void setResultPoint(Long resultPoint) {
        this.resultPoint = resultPoint;
    }

    public String getDateAttend() {
        return dateAttend;
    }

    public void setDateAttend(String dateAttend) {
        this.dateAttend = dateAttend;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
}
