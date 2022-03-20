package com.krech.botv3.domain.rest.response;

public class WordResponse {
    private int id;
    private String word;
    private String firstLetter;

    public WordResponse(int id, String word, String firstLetter) {
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
