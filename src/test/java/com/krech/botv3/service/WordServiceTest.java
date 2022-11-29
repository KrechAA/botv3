package com.krech.botv3.service;

import com.krech.botv3.domain.IndexObject;
import com.krech.botv3.domain.WordObject;
import com.krech.botv3.domain.rest.request.WordRequest;
import com.krech.botv3.repository.IndexRepository;
import com.krech.botv3.service.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.krech.botv3.repository.WordRepository;

import java.util.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class WordServiceTest {

    @Mock
    WordRepository wordRepository;

    @Mock
    IndexRepository indexRepository;


    WordService wordService;


    @BeforeEach
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
    void searchWordsForClientTestOfException() {
        String str = "феникс";

        assertThrows(IllegalArgumentException.class, () -> wordService.searchWordsForClient(str));
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
        Collections.sort(result);

        assertEquals(List.of("Феникс", "Фениксятина"), result);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(indexRepository, times(1)).findByFirstLetter(captor.capture());
        assertEquals("Ф", captor.getValue());
    }


    @Test
    void searchIndexTestOfException() {
        char[] chars = new char[]{'Ф', 'е', 'н', 'и', 'к', 'с'};

        when(indexRepository.findByFirstLetter(String.valueOf(chars[0]))).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> wordService.searchIndex(chars));
    }


    @Test
    void searchInWordsTest() {
        char[] chars2 = new char[]{'Ф', 'е', 'н', 'и', 'к', 'с'};
        WordObject fenix = new WordObject("Феникс", "Ф");
        WordObject fufaika = new WordObject("Фуфайка", "Ф");
        WordObject fenixyatina = new WordObject("Фениксятина", "Ф");

        when(wordRepository.findByFirstLetter(eq("Ф"))).thenReturn(List.of(fenix, fufaika, fenixyatina));

        List<WordObject> expected = wordService.searchInWords(chars2);

        assertEquals(expected, List.of(fenix, fenixyatina));
        verify(wordRepository, times(1)).findByFirstLetter(eq("Ф"));
    }

    @Test
    void wordHasAllCharsTest() {
        String word = "Феникс";
        char[] charsTrue = new char[]{'Ф', 'е', 'и', 'к'};
        char[] charsFalse = new char[]{'П', 'е', 'и', 'к'};

        //if second argument longer then first
        char[] charsFalse2 = new char[]{'Ф', 'е', 'и', 'к', 'е', 'и', 'к'};

        boolean resultTrue = wordService.wordHasAllChars(word, charsTrue);
        boolean resultFalse = wordService.wordHasAllChars(word, charsFalse);
        boolean resultFalse2 = wordService.wordHasAllChars(word, charsFalse2);

        assertTrue(resultTrue);
        assertFalse(resultFalse);
        assertFalse(resultFalse2);
    }

    @Test
    void saveOneWordTest() {
        WordRequest wordRequest = new WordRequest("Феникс", "Ф");
        WordObject wordObject = new WordObject(wordRequest.getWord(), wordRequest.getFirstLetter());
        WordObject wordObjectExpected = new WordObject(wordRequest.getWord(), wordRequest.getFirstLetter());

        when(wordRepository.findByName(eq(wordRequest.getWord()))).thenReturn(null);
        when(wordRepository.save(any())).thenReturn(wordObject);

        WordObject result = wordService.saveOneWord(wordRequest);

        assertEquals(wordObjectExpected.getName(), result.getName());
        assertEquals(wordObjectExpected.getFirstLetter(), result.getFirstLetter());
        verify(indexRepository, times(1)).deleteAll();
    }

    @Test
    void saveOneWordTestOfException() {
        WordRequest wordRequest = new WordRequest("Феникс", "Ф");
        WordObject wordObject = new WordObject(wordRequest.getWord(), wordRequest.getFirstLetter());

        when(wordRepository.findByName(wordRequest.getWord())).thenReturn(wordObject);
        assertThrows(IllegalArgumentException.class, () -> wordService.saveOneWord(wordRequest));
    }



    @Test
    void saveIndexTest() {

        char[] chars = new char[]{'Ф', 'е', 'и', 'к'};
        Set<WordObject> words = new HashSet<>();
        WordObject fenix = new WordObject("Феникс", "Ф");
        WordObject fenixyatina = new WordObject("Фениксятина", "Ф");
        words.add(fenix);
        words.add(fenixyatina);
        IndexObject indexObject = new IndexObject();
        indexObject.setFirstLetter("Ф");
        indexObject.setOtherLetters("еик");
        indexObject.setWords(new HashSet<>());

        when(indexRepository.findByFirstLetterAndOtherLetters("Ф", "еик")).thenReturn(indexObject);

        wordService.saveIndex(chars, words);

        ArgumentCaptor<IndexObject> captor = ArgumentCaptor.forClass(IndexObject.class);
        verify(indexRepository, times(1)).save(captor.capture());
        assertEquals("Ф", captor.getValue().getFirstLetter());
        assertEquals("еик", captor.getValue().getOtherLetters());
        assertTrue(captor.getValue().getWords().contains(fenix));
        assertTrue(captor.getValue().getWords().contains(fenixyatina));
    }

    @Test
    void saveIndexTestWhenIndexObjectEqNull() {
        char[] chars = new char[]{'Ф', 'е', 'и', 'к'};
        Set<WordObject> words = new HashSet<>();
        WordObject fenix = new WordObject("Феникс", "Ф");
        WordObject fenixyatina = new WordObject("Фениксятина", "Ф");
        words.add(fenix);
        words.add(fenixyatina);

        when(indexRepository.findByFirstLetterAndOtherLetters("Ф", "еик")).thenReturn(null);

        wordService.saveIndex(chars, words);

        ArgumentCaptor<IndexObject> captor = ArgumentCaptor.forClass(IndexObject.class);
        verify(indexRepository, times(1)).save(captor.capture());
        assertEquals("Ф", captor.getValue().getFirstLetter());
        assertEquals("еик", captor.getValue().getOtherLetters());
        assertTrue(captor.getValue().getWords().contains(fenix));
        assertTrue(captor.getValue().getWords().contains(fenixyatina));
    }

    @Test
    void saveIndexTestOfException() {
        char[] chars = new char[]{'ф', 'е', 'и', 'к'};
        Set<WordObject> words = new HashSet<>();

        assertThrows(IllegalArgumentException.class, () -> wordService.saveIndex(chars, words));
    }

    @Test
    void saveManyWordsTest() {
        List<WordObject> listOfWordObject = new ArrayList<>();
        WordObject fenix = new WordObject("Феникс", "Ф");
        WordObject fenixyatina = new WordObject("Фениксятина", "Ф");
        listOfWordObject.add(fenix);
        listOfWordObject.add(fenixyatina);

        wordService.saveManyWords(listOfWordObject);

        ArgumentCaptor<WordObject> captor = ArgumentCaptor.forClass(WordObject.class);

        verify(wordRepository, times(2)).save(captor.capture());
        assertEquals("Феникс", captor.getAllValues().get(0).getName());
        assertEquals("Ф", captor.getAllValues().get(0).getFirstLetter());
        assertEquals("Фениксятина", captor.getAllValues().get(1).getName());
        assertEquals("Ф", captor.getAllValues().get(1).getFirstLetter());
        assertEquals(2,captor.getAllValues().size());
        }

    @Test
    void updateOneWordTest() {
        WordRequest wordRequest = new WordRequest("Феникс", "Ф");
        String oldWord = "Фенекс";
        WordObject oldWordObject = new WordObject("Фенекс","Ф");

        when(wordRepository.findByName(oldWord)).thenReturn(oldWordObject);

        wordService.updateOneWord(oldWord, wordRequest);

        verify(indexRepository, times(1)).deleteAll();
        verify(wordRepository, times(1)).save(eq(oldWordObject));
        assertEquals(wordRequest.getWord(), oldWordObject.getName());
        assertEquals(wordRequest.getFirstLetter(), oldWordObject.getFirstLetter());
    }

    @Test
    void updateOneWordTestOfException() {
        WordRequest wordRequest = new WordRequest("Феникс", "Ф");
        String oldWord = "Фенекс";

        when(wordRepository.findByName(oldWord)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> wordService.updateOneWord(oldWord, wordRequest));
    }

    @Test
    void deleteOneWordTestOfException() {
        String request = "Феникс";

        when(wordRepository.findByName(request)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> wordService.deleteOneWord(request));
    }

    @Test
    void deleteOneWordTest () {
        String request = "Феникс";
        WordObject wordObject = new WordObject("Феникс","Ф");

        when(wordRepository.findByName(request)).thenReturn(wordObject);

        wordService.deleteOneWord(request);

        verify(indexRepository, times(1)).deleteAll();
        verify(wordRepository, times(1)).deleteByName(request);
    }
}
