package com.krech.botv3.domain.rest.request;

import lombok.Data;

@Data
public class WordRequest {

    private String word;
    private String firstLetter;

    public WordRequest() {
    }

    public WordRequest(String word, String firstLetter) {
        this.word = word;
        this.firstLetter = firstLetter;
    }

}
