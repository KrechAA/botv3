package com.krech.botv2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DbConfiguration {

    @Bean
    @Scope("singleton")
    public DbConnector dbConnector(){
        return new DbConnector();
    }
}
