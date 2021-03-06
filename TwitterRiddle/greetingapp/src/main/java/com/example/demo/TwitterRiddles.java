package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableScheduling
public class TwitterRiddles {

	public static void main(String[] args) {
		SpringApplication.run(TwitterRiddles.class, args);
	}

}
