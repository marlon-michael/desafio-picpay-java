package com.desafio.pixpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@EnableCaching
@SpringBootApplication
public class PixpayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PixpayApplication.class, args);
	}

}
