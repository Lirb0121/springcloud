package com.huonilaifu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.huonilaifu.service"})
public class SpringCloudFeignApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudFeignApiApplication.class, args);
	}

}
