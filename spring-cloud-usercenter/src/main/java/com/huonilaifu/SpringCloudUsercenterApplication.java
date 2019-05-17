package com.huonilaifu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@EnableCircuitBreaker
@ComponentScan(basePackages = {"com.huonilaifu.controller","com.huonilaifu.service"})
@EnableDiscoveryClient
@SpringBootApplication
public class SpringCloudUsercenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudUsercenterApplication.class, args);
	}


	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
