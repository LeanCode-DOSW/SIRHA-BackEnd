package edu.dosw.sirha.SIRHA_BackEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories 
public class SirhaBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(SirhaBackEndApplication.class, args);
	}

}
 