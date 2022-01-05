package com.krech.botv2.domain;

import java.util.Objects;

public class Indexkey {

    private char firstChar;
    private String otherChars;

    public Indexkey() {

    }
    public Indexkey(char firstChar, String otherChars) {
        this.firstChar = firstChar;
        this.otherChars = otherChars;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Indexkey)) return false;
        Indexkey indexkey = (Indexkey) o;
        return firstChar == indexkey.firstChar && otherChars.equals(indexkey.otherChars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstChar, otherChars);
    }

    public String getOtherChars(){
        return otherChars;
    }

    public char getFirstChar(){
        return firstChar;
    }
    public void setFirstChar(char firstChar) {
        this.firstChar = firstChar;
    }

    public void setOtherChars(String otherChars) {
        this.otherChars = otherChars;
    }





}

