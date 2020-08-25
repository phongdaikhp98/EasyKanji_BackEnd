package com.example.demo.DTO;

import javax.validation.constraints.NotBlank;

public class KanjiSearchDTO {
    @NotBlank
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
