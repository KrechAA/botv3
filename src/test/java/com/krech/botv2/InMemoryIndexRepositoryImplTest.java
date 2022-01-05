import com.krech.botv2.repository.InMemoryIndexRepositoryImpl;
import com.krech.botv2.domain.Indexkey;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryIndexRepositoryImplTest {
InMemoryIndexRepositoryImpl inMemoryIndexRepositoryImpl = new InMemoryIndexRepositoryImpl();


    @Test
    void add() {
        Indexkey indexkey = new Indexkey();
        Set<String> setOfString = Set.of("пипка");
        inMemoryIndexRepositoryImpl.addNewIndexAndWords(indexkey,setOfString);
        assertNotNull(inMemoryIndexRepositoryImpl.getWords(indexkey));
    }

    @Test
    void get() {

    }

    @Test
    void getAll() {
    }
}