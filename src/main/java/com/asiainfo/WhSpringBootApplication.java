package com.asiainfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WhSpringBootApplication {

	public static void main(String[] args) {

		System.setProperty("es.set.netty.runtime.available.processors","false");
		SpringApplication.run(WhSpringBootApplication.class, args);
	}
}
