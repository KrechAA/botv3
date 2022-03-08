package com.krech.botv3.service;

import com.krech.botv3.domain.IndexObject;
import com.krech.botv3.domain.WordObject;
import com.krech.botv3.domain.Indexkey;
import com.krech.botv3.repository.IndexRepository;
import com.krech.botv3.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Character.isUpperCase;

@Service
@Transactional
public class WordService {


    private final IndexRepository indexRepository;
    private final WordRepository wordRepository;


    @Autowired
    public WordService(IndexRepository indexRepository, WordRepository wordRepository) {
        this.indexRepository = indexRepository;
        this.wordRepository = wordRepository;
    }

    /**
     * Читаем слова из файла и возвращаем списком
     */
    public List<String> readWordsFromFile() throws IOException {
        List<String> list = new ArrayList<>();
        Path path = Path.of("c:\\Users\\krecha\\Downloads\\freqrnc2011.txt");
        return list = Files.readAllLines(path);

    }




    /**
     * Удаляем одно слово
     */




    /**
     * Получаем список слов и сохраняем в репозиторий
     */
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

            Indexkey check = new Indexkey(indexObject.getFirstLetter().charAt(0), indexObject.getOtherLetter());

            int lenghtOfOtherCharsPlusOne = check.getOtherChars().length() + 1;

            if (chars[0] == check.getFirstChar() & chars.length >= lenghtOfOtherCharsPlusOne) {

                Set<WordObject> setOfWordObject = indexObject.getSetOfWords();
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
     * @return
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
    public void saveIndex(char[] chars, Set<WordObject> words) {

        if (isUpperCase(chars[0])) {


            char[] charsWithoutFirstLetter = new char[chars.length - 1];
            System.arraycopy(chars, 1, charsWithoutFirstLetter, 0, chars.length - 1);

            IndexObject indexObject = indexRepository.findByFirstLetterAndOtherLetter(String.valueOf(chars[0]), new String(charsWithoutFirstLetter));
            if (indexObject == null) {
                IndexObject dbIndexObjectNew = new IndexObject();
                dbIndexObjectNew.setFirstLetter(String.valueOf(chars[0]));
                dbIndexObjectNew.setOtherLetter(String.valueOf(charsWithoutFirstLetter));
                Set<WordObject> newSetOfWordObject = new HashSet<>(words);
                dbIndexObjectNew.setWords(newSetOfWordObject);

                indexRepository.save(dbIndexObjectNew);

            } else {

                Set<WordObject> existingWords = new HashSet<>(indexObject.getSetOfWords());

                Set<WordObject> newSetOfWordObject = new HashSet<>();

                for (WordObject fromNewSet : words) {
                    if (!existingWords.contains(fromNewSet)) {

                        newSetOfWordObject.add(fromNewSet);
                    }
                }
                indexObject.getSetOfWords().addAll(newSetOfWordObject);
                indexRepository.save(indexObject);
            }


        } else {
            System.out.println("первая буква не большая");
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
