package com.abol.abol.app;

import com.abol.abol.app.contollers.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class AbolAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbolAppApplication.class, args);
	}

}
