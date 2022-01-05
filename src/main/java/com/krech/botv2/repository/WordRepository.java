package com.krech.botv2.repository;

import java.util.List;

public interface WordRepository {

    void addOneWord(String word);

    List<String> getWordsByChar(char c);
}
