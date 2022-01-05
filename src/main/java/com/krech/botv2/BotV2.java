package com.krech.botv2;

import com.krech.botv2.config.DbConnector;
import com.krech.botv2.repository.DbIndexRepositoryImpl;
import com.krech.botv2.repository.DbWordRepositoryImpl;
import com.krech.botv2.repository.WordRepository;
import com.krech.botv2.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

@SpringBootApplication(scanBasePackages = {"com.krech.botv2.repository", "com.krech.botv2.service", "com.krech.botv2.config"})
public class BotV2 implements CommandLineRunner {
    @Autowired
    private WordService wordService;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(BotV2.class);

    }

    @Override
    public void run(String... args) throws Exception {
        List<String> result = wordService.searchWordsForClient("Кмтач");
        System.out.println(String.join(", ", result));
    }
}


/*  */
    /* TODO пример последовательных заросов к боту и постройка индексов
    1) A b -> строим индекс ab

    2) A b c ->используем индекс ab и строим индекс abc

    3) A c -> строим индекс ac

    4) A b c d ->используем индекс abc и строим индекс abcd
*/

//TODO создать репо на ГХ. клонировать репо на комп (git clone, см. статью) в пустую папку.
// перенести в эту папку проект (src и pom)(в проводнике). закоммитить в git. запушить на ГХ
