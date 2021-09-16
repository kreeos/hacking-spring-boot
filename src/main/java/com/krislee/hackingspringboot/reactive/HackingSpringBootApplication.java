package com.krislee.hackingspringboot.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.thymeleaf.TemplateEngine;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class HackingSpringBootApplication {

	public static void main(String[] args) {
		BlockHound.builder()
				.allowBlockingCallsInside(
						TemplateEngine.class.getCanonicalName(), "process")
				.install();

		SpringApplication.run(HackingSpringBootApplication.class, args);
	}

}
