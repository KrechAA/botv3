package com.krech.botv3.repository;

import com.krech.botv3.domain.Indexkey;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InMemoryIndexRepositoryImpl implements IndexRepository {


    private HashMap<Indexkey, Set<String>> indexStorage = new HashMap<>();

    @Override
    public void addNewIndexAndWords(Indexkey key, Set<String> newSetOfWords){
        indexStorage.put(key, newSetOfWords);
    }

    @Override
    public Set <String> getWords(Indexkey key){

        return indexStorage.get(key); //TODO отдавать дубликат сета, а не сам сет
    }

    @Override
    public Map<Indexkey, Set<String>> getAll(){
        return indexStorage; //TODO отдавать дубликат мапы, а не саму мапу
    }

    @Override
    public void addNewWordsToExistingIndex(Indexkey indexkey, Set<String> set) {
        //todo добавление слов к уже существующему индексу
    }


//    getAll(com.krech.BotV2.domain.Indexkey){}
//    update(com.krech.BotV2.domain.Indexkey, List<String>){}
//    delete(com.krech.BotV2.domain.Indexkey){}


}
