package net.christophe.genin.spring.boot.pdfgenerator.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class PdfgeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdfgeneratorApplication.class, args);
	}
}
