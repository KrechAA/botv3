package com.krech.botv3.domain.rest.request;

public class WordResponse {

    private String word;
    private String firstLetter;

    public WordResponse(String word, String firstLetter) {
        this.word = word;
        this.firstLetter = firstLetter;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }
}
