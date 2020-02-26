package com.itechro.iaml;

import com.itechro.iaml.service.ctr.CTRService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@PropertySource("classpath:apps.properties")
public class IAMLApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(IAMLApplication.class, args);
    }


    @Bean
    public CTRService getCTRService(){
        return  new CTRService();
    }

    @Override
    public void run(String... args) throws Exception {
     //   getCTRService().generateSampleReport();
    }
}
