package com.pro.mkhub_backend;

import com.pro.mkhub_backend.security.model.CorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CorsProperties.class)
public class MkhubBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MkhubBackendApplication.class, args);
	}

}
