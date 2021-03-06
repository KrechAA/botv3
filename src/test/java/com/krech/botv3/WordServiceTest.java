package com.krech.botv3;

import com.krech.botv3.domain.IndexObject;
import com.krech.botv3.domain.WordObject;
import com.krech.botv3.repository.IndexRepository;
import com.krech.botv3.service.WordService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.krech.botv3.repository.WordRepository;

import java.util.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class) // ExtendWith - Juint; MockitoExtension - mockito. эта строка говорит junit'у, что нужно подключтить мокито к тестам, и тогда у нас заработают аннотации @Mock @InjectMocks
class WordServiceTest {

    @Mock // Mockito поля, помеченные этой аннотацией инициализируются с помощью Mockito. Изначально это пустые объекты болванки, которые ничего не делают, все методы возвращают null или пустые коллекции(стоит проверить)
    WordRepository wordRepository;


    @Mock
    IndexRepository indexRepository;


    WordService wordService;


    @BeforeEach //Junit - метод, в еоторый пожно поместитть некоторый пресетап для каждого теста в классе
    public void init() {
        wordService = new WordService(indexRepository, wordRepository);
    }

    @Test
    void searchWordsForClientTest() {
        String str = "Феиксн";

        List<IndexObject> listOfIndexObject = new ArrayList<>();

        WordObject fenix = new WordObject("Феникс", "Ф");
        WordObject fenixyatina = new WordObject("Фениксятина", "Ф");

        when(indexRepository.findByFirstLetter(eq(String.valueOf(str.charAt(0))))).thenReturn(listOfIndexObject);
        when(wordRepository.findByFirstLetter(eq(String.valueOf(str.charAt(0))))).thenReturn(List.of(fenix, fenixyatina));

        List<String> words = wordService.searchWordsForClient(str);

        List<String> expected = List.of("Феникс", "Фениксятина");

        assertEquals(expected.size(), words.size());
        assertTrue(expected.containsAll(words));
    }


    @Test
    void searchIndexTest() {
        char[] chars = new char[]{'Ф', 'е', 'н', 'и', 'к', 'с'};

        WordObject fenix = new WordObject("Феникс", "Ф");
        WordObject fenixyatina = new WordObject("Фениксятина", "Ф");
        Set<WordObject> setOfWordObjects = new HashSet<>();
        setOfWordObjects.add(fenix);
        setOfWordObjects.add(fenixyatina);

        IndexObject fenixIndex = new IndexObject();
        fenixIndex.setOtherLetters("еник");
        fenixIndex.setFirstLetter("Ф");
        fenixIndex.setWords(setOfWordObjects);

        when(indexRepository.findByFirstLetter(eq(String.valueOf(chars[0])))).thenReturn(List.of(fenixIndex));

        List<String> result = wordService.searchIndex(chars);

        assertEquals(result, List.of("Феникс", "Фениксятина"));

    }


    @Test
    void searchIndexTestOfException() {
        char[] chars = new char[]{'Ф', 'е', 'н', 'и', 'к', 'с'};

        when(indexRepository.findByFirstLetter(String.valueOf(chars[0]))).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> wordService.searchIndex(chars));
    }


    @Test
        // JUnit - помечает метод который будет рассцениваться как тест, и при сборке приложения(в нашем случае mvn clean install) метод выполтинся, если будут ошибки - сброка падает
    void searchInWordsTest() {
        char[] chars2 = new char[]{'Ф', 'е', 'н', 'и', 'к', 'с'};
        WordObject fenix = new WordObject("Феникс", "Ф");
        WordObject fufaika = new WordObject("Фуфайка", "Ф");
        WordObject fenixyatina = new WordObject("Фениксятина", "Ф");


        // Перед тем как вызвать тестируемый метод, нужно задать поведение тем методам моков, которые участвуют в логике тестируемого метода.
        when(wordRepository.findByFirstLetter(eq("Ф"))).thenReturn(List.of(fenix, fufaika, fenixyatina));


        List<WordObject> expected = wordService.searchInWords(chars2);


        assertEquals(expected, List.of(fenix, fenixyatina));
// Иногда бывает так что критерием успешного выполнения теста являются
// не возвращаемые тестируемым методом данные, а факт того, что метод мока был вызван
// в ходе выполнения тестируемого метода. Для проверки этого можно использовать verify

    }
}