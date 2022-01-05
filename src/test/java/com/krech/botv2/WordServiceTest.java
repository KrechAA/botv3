import com.krech.botv2.repository.InMemoryIndexRepositoryImpl;
import com.krech.botv2.domain.Indexkey;
import com.krech.botv2.service.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.krech.botv2.repository.WordRepository;

import java.util.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;




@ExtendWith(MockitoExtension.class) // ExtendWith - Juint; MockitoExtension - mockito. эта строка говорит junit'у, что нужно подключтить мокито к тестам, и тогда у нас заработают аннотации @Mock @InjectMocks
class WordServiceTest {

    @Mock // Mockito поля, помеченные этой аннотацией инициализируются с помощью Mockito. Изначально это пустые объекты болванки, которые ничего не делают, все методы возвращают null или пустые коллекции(стоит проверить)
    WordRepository wordRepository;

    @Mock
    InMemoryIndexRepositoryImpl inMemoryIndexRepositoryImpl;

    @Mock
    Indexkey indexkey;

    WordService wordService;

    @BeforeEach //Junit - метод, в еоторый пожно поместитть некоторый пресетап для каждого теста в классе
    public void init(){
        wordService = new WordService(inMemoryIndexRepositoryImpl, wordRepository);
    }

    @Test
    void searchWordsForClientTest(){
        String str = "Феиксн";

        List <String> expected = new ArrayList<>();
        expected.add("Фениксятина");
        expected.add("Феникс");


        HashMap<Indexkey, Set<String>> indexStorage = new HashMap<>();
        Indexkey indexkey = new Indexkey('Ф', "еникс");
        Indexkey indexkey1 = new Indexkey('Ф',"ениксятина");
        Set <String> setWithoutNumber = new HashSet<>();
        Set <String> set1 = new HashSet<>();

        setWithoutNumber.add("Феникс");
        setWithoutNumber.add("Фениксятина");
        set1.add("Феникс");
        set1.add("Фениксятина");
        set1.add("Фениксятиноподобный");
        set1.add("Фениксблаблабла");
        set1.add("Фениксовость");

        indexStorage.put(indexkey,setWithoutNumber);
        indexStorage.put(indexkey1,set1);

        when(inMemoryIndexRepositoryImpl.getAll()).thenReturn(indexStorage);

        List <String> words = wordService.searchWordsForClient(str);

        assertEquals(expected.size(), words.size());
        assertTrue(expected.containsAll(words));


    }


    @Test
    void searchIndexTest(){
        char [] chars = new char[]{'Ф','е','н','и','к','с'};
        List <String> result = null;
        char[] otherChars = new char[]{'е','н','и','к','с'};

        HashMap<Indexkey, Set<String>> indexStorage = new HashMap<>();

        when(inMemoryIndexRepositoryImpl.getAll()).thenReturn(indexStorage);
//        when(indexkey.getFirstChar()).thenReturn('Ф');
   //     when(indexkey.getOtherChars()).thenReturn("еник");
    //    when(wordService.searchInWords(eq(chars))).thenReturn(List.of("Феникс", "Фениксятина"));
    //    when(wordRepository.getWordsByChar(eq('Ф'))).thenReturn(List.of("Феникс", "Фуфайка", "Фениксятина"));

        assertThrows(NoSuchElementException.class, ()-> wordService.searchIndex(chars));
    }

    @Test
    void saveWordsTest() {
        List<String> words = new ArrayList<>();
        List<String> listInBucket = new ArrayList<>();
        words.add("Феникс");
        words.add("Фуфайка");
        words.add("Фениксятина");
        words.add("Зло");

       /* when(wordRepository.getWordsByChar(eq('Ф'))).thenReturn(List.of("Феникс", "Фуфайка", "Фениксятина"));
        when(wordRepository.getWordsByChar(eq('З'))).thenReturn(null);*/
          wordService.saveWords(words);

        verify(wordRepository, times(1)).addOneWord(eq("Феникс"));
        verify(wordRepository, times(1)).addOneWord(eq("Фуфайка"));
        verify(wordRepository, times(1)).addOneWord(eq("Фениксятина"));
     /*   verify(wordRepository, times(3)).getWordsByChar(eq('Ф'));
        verify(wordRepository, times(1)).getWordsByChar(eq('З'));*/

    }

    @Test  // JUnit - помечает метод который будет рассцениваться как тест, и при сборке приложения(в нашем случае mvn clean install) метод выполтинся, если будут ошибки - сброка падает
    void searchInWordsTest() {
        char [] chars2 = new char[]{'ф','е','н','и','к','с'};

        // Перед тем как вызвать тестируемый метод, нужно задать поведение тем методам моков, которые участвуют в логике тестируемого метода.
        when(wordRepository.getWordsByChar(eq('ф'))).thenReturn(List.of("феникс", "фуфайка", "фениксятина"));
        wordRepository.addOneWord("грифон");
        wordRepository.addOneWord("феникс");
        wordRepository.addOneWord("фуфайка");
        wordRepository.addOneWord("фениксятина");

        List <String> expected = wordService.searchInWords(chars2);
        List <String> words2 = new ArrayList<>();
        words2.add("феникс");
        words2.add("фениксятина");

       // when(chars)

        assertEquals(expected, words2);
// Иногда бывает так что критерием успешного выполнения теста являются
// не возвращаемые тестируемым методом данные, а факт того, что метод мока был вызван
// в ходе выполнения тестируемого метода. Для проверки этого можно использовать verify
        verify(wordRepository, times(1)).addOneWord(eq("грифон"));
        verify(wordRepository, times(1)).addOneWord(eq("феникс"));
        verify(wordRepository, times(1)).addOneWord(eq("фуфайка"));
        verify(wordRepository, times(1)).addOneWord(eq("фениксятина"));
    }
}