package com.krech.botv3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;



@ConditionalOnProperty(
        value="dbconfiguration.repoType",
        havingValue  = "Db")
@Configuration
//@ConfigurationProperties(prefix = "dbconfiguration")
//@PropertySource("classpath:application.properties")
public class DbConfiguration {

    @Value("${dbconfiguration.urlDb}")
    private String urlDb;
    @Value("${dbconfiguration.userDb}")
    private String userDb;
    @Value("${dbconfiguration.passDb}")
    private String passDb;



    @Bean
    @Scope("singleton")
    public DbConnector dbConnector(){
        return new DbConnector(urlDb, userDb, passDb);
    }


}
