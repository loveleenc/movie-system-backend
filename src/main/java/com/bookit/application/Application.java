package com.bookit.application;

import com.bookit.application.services.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.bookit.application.services.storage.StorageProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    @Bean
	CommandLineRunner init(StorageService storageService){

        for(int blah = 5; blah <= 30; blah ++){
            System.out.println(String.format("(%d, 'available', (SELECT price FROM seatprices WHERE theatre = 1 AND seattype = 'Bronze'), 1), ", blah));
        }
		return (args) -> {
        };
	}
}
