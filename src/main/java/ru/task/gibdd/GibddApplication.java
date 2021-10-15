package ru.task.gibdd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class GibddApplication {

	public static void main(String[] args) {
		SpringApplication.run(GibddApplication.class, args);
	}

}
