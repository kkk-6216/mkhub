package com.pro.file_storage_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class FileStorageServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileStorageServiceApplication.class, args);
	}

}
