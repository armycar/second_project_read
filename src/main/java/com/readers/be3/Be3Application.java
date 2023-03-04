package com.readers.be3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling //스케쥴러 실행
public class Be3Application {

	public static void main(String[] args) {
		SpringApplication.run(Be3Application.class, args);
	}

}