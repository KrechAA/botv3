package com.krech.botv3;

import com.krech.botv3.repository.InMemoryIndexRepositoryImpl;
import com.krech.botv3.repository.InMemoryWordRepositoryImp;
import com.krech.botv3.repository.WordRepository;
import com.krech.botv3.service.WordService;

import java.io.IOException;
import java.util.List;

public class BotV2InMemoryImpl {

    public static void main(String[] args) throws IOException {
//        InMemoryIndexRepositoryImpl inMemoryIndexRepositoryImpl = new InMemoryIndexRepositoryImpl();
//        WordRepository wordRepository = new InMemoryWordRepositoryImp();
//        WordService wordService = new WordService(inMemoryIndexRepositoryImpl, wordRepository);
//
//
//
//        wordService.saveWords(wordService.readWordsFromFile());
//
//
//
//
//        List<String> result = wordService.searchWordsForClient("Авкаотп");
//        System.out.println(result);
    }
}


    /* TODO пример последовательных заросов к боту и постройка индексов
    1) A b -> строим индекс ab

    2) A b c ->используем индекс ab и строим индекс abc

    3) A c -> строим индекс ac

    4) A b c d ->используем индекс abc и строим индекс abcd
*/


