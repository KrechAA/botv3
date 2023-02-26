package com.krech.botv3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@SpringBootApplication
       /* (scanBasePackages = {"com.krech.botv3.repository", "com.krech.botv3.service", "com.krech.botv3.controller"})*/
@EnableJpaRepositories(basePackages = "com.krech.botv3.repository")
public class BotV2 {


    public static void main(String[] args) {

        SpringApplication.run(BotV2.class);

    }
}




