package com.krech.botv2.service;

import com.krech.botv2.domain.Indexkey;
import com.krech.botv2.repository.IndexRepository;
import com.krech.botv2.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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
     * Читаем слова из файла и возвращаем списком
     */
    public List<String> readWordsFromFile() throws IOException {
        List<String> list = new ArrayList<>();
        Path path = Path.of("c:\\Users\\krech\\Downloads\\freqrnc2011.txt");
        return list = Files.readAllLines(path);

    }

    /**
     * Получаем список слов и сохраняем в репозиторий
     */
    public void saveWords(List<String> words) {
        for (String word : words) {
            String word1 = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            char firstLetter = word1.charAt(0);
      /*      if (wordRepository.getWordsByChar(firstLetter) == null) {
                List<String> listInBucket = new ArrayList<>();
                listInBucket.add(word1);
                wordRepository.addManyWords(listInBucket);
                //TODO переписать 38 строку на добавление по одному слову
            } else {


            }*/
            wordRepository.addOneWord(word1);
        }
        //save to com.krech.BotV2.repository.WordRepository
    }

    void loadWords() throws IOException {
        List<String> words = readWordsFromFile();
        saveWords(words);
    }

    /**
     * @param str список запрошенных букв первая буква списка - первая буква слова
     * @return
     */
    public List<String> searchWordsForClient(String str) {
        char[] otherChars = new char[str.length() - 1];
        if (isUpperCase(str.charAt(0)) == false) {
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
            } catch (NoSuchElementException e){
                System.out.println("нет подходящего индекса");

                Set<String> setOfWordsInDictionary = new HashSet<>(searchInWords(chars));
                saveIndex(chars, setOfWordsInDictionary);
                return new ArrayList<>(setOfWordsInDictionary);
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
        List <String> result = null;
        char[] otherChars = new char[chars.length - 1];
        int sizeOfWordList = Integer.MAX_VALUE;

        System.arraycopy(chars, 1, otherChars, 0, chars.length - 1);


            for (Map.Entry<Indexkey, Set<String>> entry : indexRepository.getAll().entrySet()) {

                Indexkey check = entry.getKey();
                int lenghtOfOtherCharsPlusOne = check.getOtherChars().length() +1;

                if (chars[0] == check.getFirstChar() & chars.length >= lenghtOfOtherCharsPlusOne) {
                    if (wordHasAllChars(check.getOtherChars(), otherChars)) {
                        if (entry.getValue().size() < sizeOfWordList) {
                            sizeOfWordList = entry.getValue().size();
                            result = new ArrayList<>(entry.getValue());

                        }
                        //TODO строки 82-84 переписать на обращения к entry, а не к репозиторию
                        //TODO кидать эксепшен, если не нашли ни одного индекса.
                        //TODO сдеать юниттест
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
    public List<String> searchInWords(char[] chars) {
        List<String> result = new ArrayList<>();

        List<String> listOfWordsInDictionary = wordRepository.getWordsByChar(chars[0]); //TODO вынуть все слова на первую букву из WordRepo и искать слова. Индексы игнорируем.
        for (String word : listOfWordsInDictionary) {
            if (wordHasAllChars(word, chars)) {
                result.add(word);
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
    void saveIndex(char[] chars, Set<String> words) {

        if (isUpperCase(chars[0])) {
            Indexkey indexkey = new Indexkey();
            indexkey.setFirstChar(chars[0]);
            char[] charsWithoutFirstLetter = new char[chars.length - 1];
            System.arraycopy(chars, 1, charsWithoutFirstLetter, 0, chars.length - 1);
            indexkey.setOtherChars(new String(charsWithoutFirstLetter));
            if (indexRepository.getWords(indexkey).size() == 0) { //TODO заменить getAll на get. ищм в репо индекс по индекскею. пустые индексы не сохраняем
                indexRepository.addNewIndexAndWords(indexkey, words);
            } else {
                indexRepository.getWords(indexkey).addAll(words);//TODO заменить addAll на add в цикле.
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
