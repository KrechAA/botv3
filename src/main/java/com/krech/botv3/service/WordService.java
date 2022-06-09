package com.krech.botv3.service;

import com.krech.botv3.domain.IndexObject;
import com.krech.botv3.domain.WordObject;
import com.krech.botv3.domain.rest.request.WordRequest;
import com.krech.botv3.repository.IndexRepository;
import com.krech.botv3.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Character.isUpperCase;

@Service
public class WordService {


    private final IndexRepository indexRepository;
    private final WordRepository wordRepository;


    @Autowired
    public WordService(IndexRepository indexRepository, WordRepository wordRepository) {
        this.indexRepository = indexRepository;
        this.wordRepository = wordRepository;
    }



    /**
     *
     * @param wordRequest request from client
     * @return wordObject to controller
     */
    @Transactional
    public WordObject saveOneWord(WordRequest wordRequest) {
        if (wordRepository.findByName(wordRequest.getWord()) != null) {
            throw new IllegalArgumentException("This word already exist");
        }
        WordObject wordObject = new WordObject(wordRequest.getWord(), wordRequest.getFirstLetter());
        indexRepository.deleteAll();
        return wordRepository.save(wordObject);
    }

    /**
     *
     * @param request request from client to delete one word in repository
     */
    @Transactional
    public void deleteOneWord(String request){
        if (wordRepository.findByName(request) == null) {
            throw new IllegalArgumentException("This word not found");
        } else {
            indexRepository.deleteAll();
            wordRepository.deleteByName(request);
        }
    }

    /**
     *
     * @param oldWord word before update
     * @param newWord request for update
     * @return updated wordObject
     */
    @Transactional
    public WordObject updateOneWord (String oldWord, WordRequest newWord){

        WordObject oldWordObject = wordRepository.findByName(oldWord);
        if (oldWordObject == null) {
            throw new IllegalArgumentException("Your word is not found");
        } else {
            oldWordObject.setName(newWord.getWord());
            oldWordObject.setFirstLetter(newWord.getFirstLetter());
            indexRepository.deleteAll();
            wordRepository.save(oldWordObject);
            return oldWordObject;
        }
    }


    /**
     * Получаем список слов и сохраняем в репозиторий
     */
    @Transactional
    public void saveManyWord(List<WordObject> listOfWordObject) {
        for (WordObject wordObject : listOfWordObject) {
            wordRepository.save(wordObject);

        }
    }


    /**
     * @param str список запрошенных букв первая буква списка - первая буква слова
     * @return result
     */
    public List<String> searchWordsForClient(String str) {
        char[] otherChars = new char[str.length() - 1];
        if (!isUpperCase(str.charAt(0))) {
            throw new IllegalArgumentException("Первая буква не большая");
        } else {
            char[] firstLetter = new char[1];
            firstLetter[0] = str.charAt(0);
            char[] chars = str.toCharArray();
            System.arraycopy(chars, 1, otherChars, 0, chars.length - 1);
            Arrays.sort(otherChars);

            char[] charsSort = new char[chars.length];
            System.arraycopy(firstLetter, 0, charsSort, 0, firstLetter.length);
            System.arraycopy(otherChars, 0, charsSort, firstLetter.length, otherChars.length);

            try {
                return searchIndex(charsSort);
            } catch (NoSuchElementException e) {
                System.out.println("нет подходящего индекса");

                Set<WordObject> setOfWordsInDictionary = new HashSet<>(searchInWords(chars));
                saveIndex(chars, setOfWordsInDictionary);


                return setOfWordsInDictionary.stream().map(WordObject::getName).collect(Collectors.toList());
            }
        }

    }


    /**
     * метод ищет наиболее подходящий индекс по запрошенным буквам.
     * если индекс не найдет, то кидаем NoSuchElementException
     *
     * @param chars список запрошенных букв первая буква списка - первая буква слова
     * @return result
     */
    public List<String> searchIndex(char[] chars) throws NoSuchElementException {
        List<String> result = null;

        int sizeOfWordList = Integer.MAX_VALUE;

        List<IndexObject> listOfIndexObject = indexRepository.findByFirstLetter(String.valueOf(chars[0]));
        if (listOfIndexObject == null) {
            throw new NoSuchElementException();
        }
        for (IndexObject indexObject : listOfIndexObject) {
            int lenghtOfOtherCharsPlusOne = indexObject.getOtherLetters().length() + 1;

            if (String.valueOf(chars[0]).equals(indexObject.getFirstLetter()) & chars.length >= lenghtOfOtherCharsPlusOne) {
                Set<WordObject> setOfWordObject = indexObject.getWords();
                for (WordObject wordObject1 : setOfWordObject) {
                    if (wordHasAllChars(wordObject1.getName(), chars)) {
                        if (setOfWordObject.size() < sizeOfWordList) {
                            sizeOfWordList = setOfWordObject.size();
                            result = new ArrayList<>();
                            for (WordObject wordObject : setOfWordObject) {
                                result.add(wordObject.getName());
                            }
                        }

                    }
                }

            }
        }
        if (result == null) {
            throw new NoSuchElementException();
        }

        return result;
    }

    /**
     * @param chars список запрошенных букв первая буква списка - первая буква слова
     * @return list of word objects to parent method
     */
    public List<WordObject> searchInWords(char[] chars) {
        List<WordObject> result = new ArrayList<>();

        List<WordObject> listOfWordsInDictionary = wordRepository.findByFirstLetter(String.valueOf(chars[0]));
        for (WordObject wordObject : listOfWordsInDictionary) {
            String word = wordObject.getName();
            if (wordHasAllChars(word, chars)) {
                result.add(wordObject);
            }
        }
        return result;
    }

    /**
     * @param chars список запрошенных букв первая буква списка - первая буква слова
     * @param words список слов найденных по запрошенным буквам
     */
    @Transactional
    public void saveIndex(char[] chars, Set<WordObject> words) {

        if (!isUpperCase(chars[0])) {
            throw new IllegalArgumentException("Первая буква не большая");
        } else {
            char[] charsWithoutFirstLetter = new char[chars.length - 1];
            System.arraycopy(chars, 1, charsWithoutFirstLetter, 0, chars.length - 1);

            IndexObject indexObject = indexRepository.findByFirstLetterAndOtherLetters(String.valueOf(chars[0]), new String(charsWithoutFirstLetter));
            if (indexObject == null) {
                IndexObject dbIndexObjectNew = new IndexObject();
                dbIndexObjectNew.setFirstLetter(String.valueOf(chars[0]));
                dbIndexObjectNew.setOtherLetters(String.valueOf(charsWithoutFirstLetter));
                dbIndexObjectNew.setWords(words);

                indexRepository.save(dbIndexObjectNew);

            } else {

                indexObject.getWords().addAll(words);
                indexRepository.save(indexObject);
            }

        }


    }

    private boolean wordHasAllChars(String word, char[] chars) {
        int count = 0;
        char[] charsForOperations = new char[chars.length];
        System.arraycopy(chars, 0, charsForOperations, 0, chars.length);
        char[] charsOfWords = word.toCharArray();
        if (charsOfWords.length < chars.length) {

            return false;
        }
        for (int i = 0; i < chars.length; i++) {
            for (int j = 0; j < charsOfWords.length; j++) {
                if (charsForOperations[i] == charsOfWords[j]) {
                    count++;
                    charsForOperations[i] = '2';
                    charsOfWords[j] = '1';
                    break;
                }
            }
        }

        return chars.length == count;

    }
}
