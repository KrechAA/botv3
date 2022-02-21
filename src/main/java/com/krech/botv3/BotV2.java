package com.krech.botv3;

import com.krech.botv3.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.List;


@SpringBootApplication
        (scanBasePackages = {"com.krech.botv3.repository", "com.krech.botv3.service", "com.krech.botv3.config"})
@EnableJpaRepositories(basePackages = "com.krech.botv3.repository")
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

