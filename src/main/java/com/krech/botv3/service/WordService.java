package com.krech.botv3.service;

import com.krech.botv3.domain.IndexObject;
import com.krech.botv3.domain.WordObject;
import com.krech.botv3.domain.Indexkey;
import com.krech.botv3.repository.IndexRepository;
import com.krech.botv3.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Value("${dbconfiguration.repoType}")
    private String repoType;

    @Autowired
    public WordService(IndexRepository indexRepository, WordRepository wordRepository) {
        this.indexRepository = indexRepository;
        this.wordRepository = wordRepository;
    }


    public void preparingRepo() throws IOException {
         saveWords(readWordsFromFile());
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
     * Получаем список слов и сохраняем в репозиторий
     */
    public void saveWords(List<String> words) {
        for (String word : words) {
            String word1 = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            WordObject wordObject = new WordObject(word1, word1.substring(0, 1));
      /*      if (wordRepository.getWordsByChar(firstLetter) == null) {
                List<String> listInBucket = new ArrayList<>();
                listInBucket.add(word1);
                wordRepository.addManyWords(listInBucket);
                //TODO переписать 38 строку на добавление по одному слову
            } else {


            }*/
            wordRepository.save(wordObject);
        }
        //save to com.krech.BotV2.repository.WordRepository
    }

//    void loadWords() throws IOException {
//        List<String> words = readWordsFromFile();
//        saveWords(words);
//    }

    /**
     * @param str список запрошенных букв первая буква списка - первая буква слова
     * @return
     */
    public List<String> searchWordsForClient(String str) {
        char[] otherChars = new char[str.length() - 1];
        if (!isUpperCase(str.charAt(0))) {
            System.out.println("Первая буква не большая");
            //дописать возвращение к началу. добавить исключение? добавить сканнер и цикл?
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
//TODO собрать здесь всю логику бота, используя уже написанные  методы + дописать недостающую логику.


//      1)ищем наиболее подходящий индекс
//              1а) Если нет ищем сразу по словам
//      2)ищем слово в индексе по буквам из запроса
//      3)строим индекс по буквам из запроса
//      4) возвращаем список слов
        return null;
    }


    /**
     * метод ищет наиболее подходящий индекс по запрошенным буквам.
     * если индекс не найдет, то кидаем NoSuchElementException
     *
     * @param chars список запрошенных букв первая буква списка - первая буква слова
     * @return
     */
    public List<String> searchIndex(char[] chars) throws NoSuchElementException {
        List<String> result = null;
        char[] otherChars = new char[chars.length - 1];
        int sizeOfWordList = Integer.MAX_VALUE;

        System.arraycopy(chars, 1, otherChars, 0, chars.length - 1);

        List<IndexObject> listOfIndexObject = indexRepository.findByFirstLetter(String.valueOf(chars[0]));
        if (listOfIndexObject == null) {
            throw new NoSuchElementException();
        }
        for (IndexObject indexObject : listOfIndexObject) { //TODO getAll переделать в findByFirstLetter, переписать цикл (файнд возвращает другое). Избавиться от n+1 проблемы (отключить lazy в dbindexobject)


            Indexkey check = new Indexkey(indexObject.getFirstLetter().charAt(0), indexObject.getOtherLetter());

            int lenghtOfOtherCharsPlusOne = check.getOtherChars().length() + 1;

            if (chars[0] == check.getFirstChar() & chars.length >= lenghtOfOtherCharsPlusOne) {

               Set <WordObject> setOfWordObject = indexObject.getSetOfWords();
                for (WordObject wordObject1 : setOfWordObject) {
                    if (wordHasAllChars(wordObject1.getName(), chars)) {
                        if (setOfWordObject.size() < sizeOfWordList) {
                            sizeOfWordList = setOfWordObject.size();
                            result = new ArrayList<>();
                            for (WordObject wordObject : setOfWordObject) {
                                result.add(wordObject.getName());
                            }
                        }
                        //TODO строки 82-84 переписать на обращения к entry, а не к репозиторию
                        //TODO кидать эксепшен, если не нашли ни одного индекса.
                        //TODO сдеать юниттест
                    }
                }

            }
        }
        if (result == null) {
            throw new NoSuchElementException();
        }

        // если нет подходящего индекса кидаем new NoSuchElementException
        return result;
    }

    /**
     * @param chars список запрошенных букв первая буква списка - первая буква слова
     * @return
     */
    public List<WordObject> searchInWords(char[] chars) {
        List<WordObject> result = new ArrayList<>();

        List<WordObject> listOfWordsInDictionary = wordRepository.findByFirstLetter(String.valueOf(chars[0])); //TODO вынуть все слова на первую букву из WordRepo и искать слова. Индексы игнорируем.
        for (WordObject wordObject : listOfWordsInDictionary) {
            String word = wordObject.getName();
            if (wordHasAllChars(word, chars)) {
                result.add(wordObject);
            }
        }

        return result;
        //поиск напора слов по запрощенным буквам
        //вынуть все слова из WordRepo
    }

    /**
     * @param chars список запрошенных букв первая буква списка - первая буква слова
     * @param words список слов найденных по запрошенным буквам
     */
    void saveIndex(char[] chars, Set<WordObject> words) {

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

                //              indexRepository.addNewIndexAndWords(indexkey, words); //TODO создать новый объект индекса и сделать save
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


                //TODO в найденный индекс добавляем слова и делаем save
            }


        } else {
            System.out.println("первая буква не большая");
            //вывести сообщение о некорректном вводе запроса
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

        //проверка слова на содержание всех чаров
    }

}


/* (Арбуз)
Абру
ОБЩИЙ АЛГОРИТМ ПОИСКА:
А - берем все слова на А (WordRepo)
бру - ищем все слова, которые содержат б, р, у. Используем метод wordHasAllChars

Индекс - это сам запрос без первой буквы. Строим индекс из всех букв запроса первая буква+отсортированное все остальное.

Абрзу
Когда приходят со вторым запросом, то ищем, есть ли такой индекс или наиболее подходящий (по количеству совпадающих букв. Меньшие
индексы отметаются). И ищем аналогично слова по наличию всех букв в слове (Не по списку слов из WordRepo, а по списку из IndexRepo)
После обработки каждого запроса результаты сохранять в IndexRepo. Из всех букв запроса строится новый индекс.

ПОИСК ЛУЧШЕГО ИНДЕКСА:
Абзру

Аб
Абру
Абкру
В индекс входят только те буквы, которые есть в запросе в нужном количестве. Используем метод wordHasAllChars
Из нескольких индеков, которые удовлетворяют первому условию, выбирается тот, к которому привязано наименьшее количество слов.
Сравниваем текущий индекс и следующий. Если следующий лучше текущего, то делаем его текущим. Повторять до конца списка индексов.
Итерируемся по всем индексам -> проверяем с помощью wordHasAllChars -> храним только лучший по количеству слов индекс -> если текущий
индекс лучше сохраненного лучшего индекса, то сохраняем вместо лучшего индекса текущий.


БЕЗОПАСНОСТЬ:
хранилища индексов и слов сделать private. прописать соответствующие методы в классах IndexRepo и WordRepo.

 */
