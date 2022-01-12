package com.krech.botv3.repository;

import com.krech.botv3.domain.Indexkey;

import java.util.Map;
import java.util.Set;

public interface IndexRepository {

   public void addNewIndexAndWords(Indexkey key, Set<String> newSetOfWords);

    public Set <String> getWords(Indexkey key);

    public Map<Indexkey, Set<String>> getAll ();

    public void addNewWordsToExistingIndex(Indexkey indexkey, Set <String> set);

}
