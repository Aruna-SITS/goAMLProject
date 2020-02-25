package com.itechro.iaml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@PropertySource("classpath:apps.properties")
public class IAMLApplication {

    public static void main(String[] args) {
        SpringApplication.run(IAMLApplication.class, args);
    }

}
