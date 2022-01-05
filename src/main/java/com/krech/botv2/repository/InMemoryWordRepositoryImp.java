package com.krech.botv2.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryWordRepositoryImp implements WordRepository {
    // Хранилище слов


    private HashMap<Character, List<String>> wordStorage = new HashMap<>();



    @Override
    public void addOneWord(String word) {
        if (wordStorage.containsKey(word.charAt(0))) {
            wordStorage.get(word.charAt(0)).add(word);
        } else {
            List<String> newList = new ArrayList<>();
            newList.add(word);
            wordStorage.put(word.charAt(0), newList);
        }

    }

    @Deprecated
    void addManyWords(List<String> words) {
        wordStorage.put(words.get(0).charAt(0), words);

    }


    @Override
    public List<String> getWordsByChar(char c) {
        return wordStorage.get(c);
    }
//    delete(String word){}

}
